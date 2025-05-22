package com.homework.library_management.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.homework.library_management.dto.DTO_AuthReq;

import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.AccountRepository;
import com.homework.library_management.repositories.LibrarianRepository;
import com.homework.library_management.utils.DateTimeHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
	AccountRepository accountRepository;
	LibrarianRepository librarianRepository;

	public void authenticate(DTO_AuthReq dto, HttpServletRequest request) throws AppException {
		var account = accountRepository.findByEmail(dto.getEmail())
			.orElseThrow(() -> new AppException("Email không tồn tại"));

		if (Objects.isNull(account) || !account.getPassword().equals(dto.getPassword()))
			throw new AppException("Thông tin không đúng");

		var librarian = librarianRepository.findByAccountAccountId(account.getAccountId())
			.orElseThrow(() -> new AppException("Thủ thư không tồn tại"));

		var authTime = DateTimeHelper.localDateTimeToStr(LocalDateTime.now().plus(2, ChronoUnit.HOURS));
		request.getSession().setAttribute("librarianId", librarian.getLibrarianId().toString());
		request.getSession().setAttribute("expiredAt", authTime);
		request.getSession().setAttribute("clientIp", request.getRemoteAddr());
    }

}
