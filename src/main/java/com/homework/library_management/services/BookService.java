package com.homework.library_management.services;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_AddBookReq;
import com.homework.library_management.dto.DTO_UpdateBookReq;
import com.homework.library_management.entities.Book;
import com.homework.library_management.entities.BookGenre;
import com.homework.library_management.entities.Genre;
import com.homework.library_management.enums.PageEnum;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.BookBorrowingRequestRepository;
import com.homework.library_management.repositories.BookGenreRepository;
import com.homework.library_management.repositories.BookRepository;
import com.homework.library_management.repositories.GenreRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    GlobalLogger logger;
    BookRepository bookRepository;
    GenreRepository genreRepository;
    BookGenreRepository bookGenreRepository;
    LibrarianService librarianService;
    private final BookBorrowingRequestRepository bookBorrowingRequestRepository;

    public void prepareManageBook(HttpServletRequest request, int page, String query) {
        logger.handling(request, "BookService.prepareManageBook");
        librarianService.attachLibrarianInfo(request);
        page = page < 0 ? 0 : page - 1;
        Page<Book> books = bookRepository.findAllByBookName(query, PageRequest.of(page, PageEnum.SIZE.getSize()));

        if (!query.isEmpty())
            request.setAttribute("query", query);
        request.setAttribute("genres", genreRepository.findAll());
        request.setAttribute("books", books.stream().toList());
        request.setAttribute("currentPage", page + 1);
        request.setAttribute("totalPages", books.getTotalPages());
    }

    public void showDetailBook(HttpServletRequest request, Long id) throws AppException {
        logger.handling(request, "BookService.showDetailBook");
        var book = bookRepository.findById(id)
            .orElseThrow(() -> new AppException("Không tìm thấy sách"));

        var builtIdGenres = new StringBuilder();
        for (BookGenre bg: book.getBookGenres())
            builtIdGenres.append(bg.getGenre().getGenreName()).append(",");

        request.setAttribute("allGenres", genreRepository.findAll());
        request.setAttribute("book", book);
        request.setAttribute("genresOfBook", book.getBookGenres().stream().map(BookGenre::getGenre).toList());
        request.setAttribute("bookStatus", book.getStatus());
        request.setAttribute("builtGenres", builtIdGenres.substring(0, builtIdGenres.length() - 1));
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public void addBook(HttpServletRequest request, DTO_AddBookReq dto) throws AppException {
        logger.handling(request, "BookService.addBook");
        var genres = genreRepository.findAllById(dto.getGenres());
        if (genres.size() != dto.getGenres().size())
            throw new AppException("Thể loại không hợp lệ");

        Book book;
        try {
            book = bookRepository.save(Book.builder()
                .bookName(dto.getBookName())
                .availableQuantity(dto.getAvailableQuantity())
                .description(dto.getDescription())
                .imgUrl("https://baokhanhhoa.vn/file/e7837c02857c8ca30185a8c39b582c03/052023/rar_24_20230525114955.jpeg")
                .authors(dto.getAuthors())
                .status(1)
                .build());
        } catch (DataIntegrityViolationException e) {
            throw new AppException("Sách đã tồn tại");
        }

        bookGenreRepository.saveAll(genres.stream().map(genre -> BookGenre.builder()
            .book(book).genre(genre).build()).toList());
    }

    public void updateBook(HttpServletRequest request, DTO_UpdateBookReq dto) throws AppException {
        try {
            this.updateBookCore(request, dto);
        } catch (DataIntegrityViolationException e) {
            throw new AppException("Tên sách trùng tên với một cuốn khác");
        }
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public void updateBookCore(HttpServletRequest request, DTO_UpdateBookReq dto)
        throws AppException, DataIntegrityViolationException {
        logger.handling(request, "BookService.updateBook");

        var newGenres = genreRepository.findAllById(dto.getGenres());
        if (newGenres.size() != dto.getGenres().size())
            throw new AppException("Thể loại không hợp lệ");

        Book newBook;
        Book updatedBook = bookRepository.findById(dto.getBookId())
            .orElseThrow(() -> new AppException("Sách không tồn tại"));

        if (bookBorrowingRequestRepository.existsByBookIdAndNotReturnYet(dto.getBookId()))
            throw new AppException("Sách này đang được mượn nên không thể cập nhật");

        updatedBook.setBookName(dto.getBookName());
        updatedBook.setAuthors(dto.getAuthors());
        updatedBook.setAvailableQuantity(dto.getAvailableQuantity());
        updatedBook.setDescription(dto.getDescription());
        updatedBook.setStatus(dto.getStatus());
        newBook = bookRepository.save(updatedBook); //--"save" with an entity has "id" will update it.

        var newGenresMap = newGenres.stream().collect(Collectors.toMap(Genre::getGenreId, Function.identity()));
        //--Genre has been changed
        if (newBook.getBookGenres().size() != newGenres.size()
            || !newBook.getBookGenres().stream().allMatch(bg -> newGenresMap.containsKey(bg.getGenre().getGenreId()))) {
            //--This
            bookGenreRepository.deleteAllInBatch(newBook.getBookGenres());
            bookGenreRepository.flush();    //--Actually call .remove() (SQL).
            newBook.getBookGenres().clear();    //--Clear old relationship in Parent-Entity (Book).
            bookGenreRepository.saveAll(newGenres.stream()
                .map(genre -> BookGenre.builder().book(newBook).genre(genre).build())
                .toList());
        }
    }
}
