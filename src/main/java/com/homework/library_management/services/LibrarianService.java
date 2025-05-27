package com.homework.library_management.services;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_UpdateFullName;
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
    GlobalLogger logger;
    LibrarianRepository librarianRepository;

    public void attachLibrarianInfo(HttpServletRequest request) throws AppException {
        logger.handling(request, "LibrarianService.attachLibrarianInfo(for HeaderUI)");
        Long id = Long.parseLong(request.getSession().getAttribute("librarianId").toString());
        //--Always found "id", because it filtered by interceptor.
        var librarian = librarianRepository.findById(id)
            .orElseThrow(() -> new AppException("Phiên đăng nhập hết hạn_/home"));
        request.setAttribute("librarian", librarian.getFullName());
    }

    public void prepareDataToShowUserInfoPage(HttpServletRequest request) throws AppException {
        logger.handling(request, "LibrarianService.prepareDataToShowUserInfoPage");
        Long id = Long.parseLong(request.getSession().getAttribute("librarianId").toString());
        //--Always found "id", because it filtered by interceptor.
        var librarian = librarianRepository.findById(id)
            .orElseThrow(() -> new AppException("Phiên đăng nhập hết hạn_/login"));
        request.setAttribute("librarianId", librarian.getLibrarianId());
        request.setAttribute("employeeId", librarian.getEmployeeId());
        request.setAttribute("librarian", librarian.getFullName());
        request.setAttribute("fullName", librarian.getFullName());
        request.setAttribute("email", librarian.getAccount().getEmail());
    }

    public void updateLibrarianFullName(HttpServletRequest request, DTO_UpdateFullName dto) {
        logger.handling(request, "LibrarianService.updateLibrarianFullName");
        Long id = Long.parseLong(request.getSession().getAttribute("librarianId").toString());
        //--Always found "id", because it filtered by interceptor.
        var librarian = librarianRepository.findById(id)
            .orElseThrow(() -> new AppException("Phiên đăng nhập hết hạn_/login"));
        librarian.setFullName(dto.getFullName());
        librarianRepository.save(librarian);
    }
}
