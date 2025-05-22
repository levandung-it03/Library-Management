package com.homework.library_management.services;

import com.homework.library_management.dto.DTO_BorrowingBook;
import com.homework.library_management.dto.DTO_HistoriesResponse;
import com.homework.library_management.entities.Book;
import com.homework.library_management.entities.BookBorrowingRequest;
import com.homework.library_management.entities.BorrowingRequest;
import com.homework.library_management.enums.PageEnum;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.*;
import com.homework.library_management.utils.DateTimeHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowingRequestService {
    String SEPARATOR = "_\\[separator]_";
    BookRepository bookRepository;
    LibrarianRepository librarianRepository;
    BorrowingRequestRepository borrowingRequestRepository;
    BookBorrowingRequestRepository bookBorrowingReqRepo;
    LibrarianService librarianService;
    GenreRepository genreRepository;
    MembershipCardRepository membershipCardRepository;
    private final BookBorrowingRequestRepository bookBorrowingRequestRepository;

    public void prepareBorrowingBook(HttpServletRequest request, int page, String query, String membershipCard) {
        librarianService.attachLibrarianInfo(request);
        page = page < 0 ? 0 : page - 1;
        Page<Book> books = bookRepository.findAllAvailableBooksByBookName(query,
            PageRequest.of(page, PageEnum.SIZE.getSize()));

        if (!query.isEmpty())
            request.setAttribute("query", query);
        request.setAttribute("genres", genreRepository.findAll());
        request.setAttribute("books", books.stream().toList());
        request.setAttribute("currentPage", page + 1);
        request.setAttribute("totalPages", books.getTotalPages());

        if (!membershipCard.isEmpty()) {
            var allRequests = bookBorrowingReqRepo.findAllByBorrowingRequest_MembershipCard_MembershipCard(membershipCard);
            if (allRequests.isEmpty())
                throw new AppException("Mã này không nợ sách_/borrowing-book");
            var serializedBooks = new StringBuilder();
            serializedBooks.append("[");
            for (BookBorrowingRequest borrowingReq : allRequests) {
                serializedBooks
                    .append("{\"bookName\":\"")
                    .append(borrowingReq.getBook().getBookName())
                    .append("\",\"quantity\":\"")
                    .append(borrowingReq.getQuantity())
                    .append("\"},");
            }
            serializedBooks.deleteCharAt(serializedBooks.length() - 1);
            serializedBooks.append("]");
            request.setAttribute("serializedBorrowingBooks", serializedBooks.toString());
        }
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public void createBorrowingRequest(HttpServletRequest request, DTO_BorrowingBook dto) {
        if (borrowingRequestRepository.hasMembershipCardNotReturnYet(dto.getMembershipCard()))
            throw new AppException("Thẻ này vẫn còn sách chưa trả nên không thể mượn_/borrowing-book");

        Long librarianId = Long.parseLong(request.getSession().getAttribute("librarianId").toString());
        var librarian = librarianRepository.findById(librarianId)
            .orElseThrow(() -> new AppException("Phiên đăng nhập hết hạn_/borrowing-book"));
        var membershipCard = membershipCardRepository.findById(dto.getMembershipCard())
            .orElseThrow(() -> new AppException("Thẻ thư viện không tồn tại_/borrowing-book"));
        if (membershipCard.getProhibited().equals(1))
            throw new AppException("Thẻ thư viện đang bị cấm thao tác_/borrowing-book");
        var borrowingRequest = borrowingRequestRepository.save(BorrowingRequest.builder()
            .librarian(librarian)
            .borrowingTime(LocalDateTime.now())
            .returnedTime(null)
            .returningStatus(0)
            .membershipCard(membershipCard)
            .build());

        var bookIds = new ArrayList<Long>();
        var bookQuantityMap = new HashMap<Long, Integer>();
        try {
            dto.getBorrowedBooks().forEach(pair -> {
                var extractedPair = pair.split(SEPARATOR);
                var bookId = Long.parseLong(extractedPair[0]);
                bookIds.add(bookId);
                bookQuantityMap.put(bookId, Integer.parseInt(extractedPair[1]));
            });
        } catch (NumberFormatException | NullPointerException | IndexOutOfBoundsException e) {
            throw new AppException("Thông tin sách được mượn không hợp lệ_/borrowing-book");
        }
        List<Book> borrowedBooks = bookRepository.findAllById(bookIds);
        if (bookIds.size() != dto.getBorrowedBooks().size())
            throw new AppException("Thông tin sách được mượn không hợp lệ_/borrowing-book");
        bookBorrowingReqRepo.saveAll(borrowedBooks.stream().map(book -> BookBorrowingRequest.builder()
            .borrowingRequest(borrowingRequest)
            .book(book)
            .quantity(bookQuantityMap.get(book.getBookId()))
            .build()
        ).toList());
        bookRepository.saveAll(borrowedBooks.stream().peek(book -> {
            var theRestQuantity = book.getAvailableQuantity() - bookQuantityMap.get(book.getBookId());
            if (theRestQuantity < 0)    throw new AppException("Số lượng mượn vượt quá số lượng có_/borrowing-book");
            book.setAvailableQuantity(theRestQuantity);
        }).toList());
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public void createReturningRequest(String membershipCard) {
        if (!membershipCardRepository.isValidMembershipCard(membershipCard))
            throw new AppException("Thẻ thư viện không tồn tai, hoặc đang bị cấm thao tác_/borrowing-book");
        var oldBorrowingRequest = borrowingRequestRepository
            .findCurrentBorrowingRequestOfMembership(membershipCard)
            .orElseThrow(() -> new AppException("Mã thẻ này không còn nợ sách_/borrowing-book"));
        var borrowedBooksThatReturnedQuantity = oldBorrowingRequest.getBookBorrowingRequests().stream()
            .map(bbr -> {
                //--Return books.availableQuantity when Customer returns books.
                bbr.getBook().setAvailableQuantity(bbr.getBook().getAvailableQuantity() + bbr.getQuantity());
                return bbr.getBook();
            }).toList();
        bookRepository.saveAll(borrowedBooksThatReturnedQuantity);
        oldBorrowingRequest.setReturnedTime(LocalDateTime.now());
        oldBorrowingRequest.setReturningStatus(1);
        borrowingRequestRepository.save(oldBorrowingRequest);
    }

    public String getBorrowingHistory(String membershipCard) {
        StringBuilder response = new StringBuilder("{\"data\":[");
        var queryResult = bookBorrowingRequestRepository.findAllHistoriesByMembershipCard(membershipCard);
        var resultList = new ArrayList<String>();
        for (int i = 0; i < queryResult.size(); i++) {
            var mappedObject = DTO_HistoriesResponse.builder()
                .borrowingRequestId(Long.parseLong(queryResult.get(i)[0].toString()))
                .borrowingTime(DateTimeHelper.localDateTimeToStr((LocalDateTime) queryResult.get(i)[1]))
                .returnedTime(queryResult.get(i)[2] == null ? null
                    : DateTimeHelper.localDateTimeToStr((LocalDateTime) queryResult.get(i)[2]))
                .build();
            var books = new ArrayList<DTO_HistoriesResponse.DTO_BookOfHistoriesResponse>();
            do {
                books.add(DTO_HistoriesResponse.DTO_BookOfHistoriesResponse.builder()
                    .bookName(queryResult.get(i)[3].toString())
                    .borrowedQuantity(Integer.parseInt(queryResult.get(i)[4].toString()))
                    .build());
                i++;
            } while (i < queryResult.size() && queryResult.get(i - 1)[0].equals(queryResult.get(i)[0]));
            mappedObject.setBooks(books);
            resultList.add(mappedObject.toString());
            i--;    //--Turn back
        }
        response.append(String.join(", ", resultList));
        return response.append("]}").toString();
    }
}
