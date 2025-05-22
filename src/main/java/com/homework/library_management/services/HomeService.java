package com.homework.library_management.services;

import com.homework.library_management.repositories.BookBorrowingRequestRepository;
import com.homework.library_management.repositories.BookRepository;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeService {
    LibrarianService librarianService;
    BookRepository bookRepository;
    BookBorrowingRequestRepository bookBorrowingRequestRepository;

    public void prepareStatistic(HttpServletRequest request) {
        librarianService.attachLibrarianInfo(request);
        long current = APIHelper.toValidNum(bookRepository.countTotalCurrentBooks()),
            borrowed = APIHelper.toValidNum(bookBorrowingRequestRepository.countTotalBorrowedBooks());
        request.setAttribute("totalBooks", current + borrowed);
        request.setAttribute("availableBooks", APIHelper.toValidNum(bookRepository.countTotalAvailableBooks()));
        request.setAttribute("borrowedBooks", borrowed);
        request.setAttribute("lockedBooks", APIHelper.toValidNum(bookRepository.countTotalLockedBooks()));
    }

}
