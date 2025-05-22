package com.homework.library_management.services;

import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.LibrarianRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LibrarianService {
    LibrarianRepository librarianRepository;

    public void attachLibrarianInfo(HttpServletRequest request) throws AppException {
        Long id = Long.parseLong(request.getSession().getAttribute("librarianId").toString());
        var librarian = librarianRepository.findById(id)    //--Always found "id", because it filtered by interceptor.
            .orElseThrow(() -> new AppException("Phiên đăng nhập hết hạn_/login"));
        request.setAttribute("librarian", librarian.getFullName());
    }
}
