package com.homework.library_management.config;

import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.LibrarianRepository;
import com.homework.library_management.utils.DateTimeHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppInterceptor implements HandlerInterceptor {
    LibrarianRepository librarianRepository;
    String[] PASSED_PATH = {
        "/authenticate",
        "/login",
        "/favicon.ico",
        "/error",    //--Default from Spring
    };

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        log.info("URL: {}, method: {}", request.getRequestURI(), request.getMethod());

        try {
            if (Arrays.asList(PASSED_PATH).contains(request.getRequestURI()))
                return true;
            Object librarianId = request.getSession().getAttribute("librarianId");
            if (librarianRepository.findById(Long.parseLong(librarianId.toString())).isEmpty())
                throw new AppException("Không tìm thấy Thủ thư");

            String clientIp = request.getSession().getAttribute("clientIp").toString();
            String currentIp = request.getRemoteAddr();
            if (!currentIp.equals(clientIp))
                throw new AppException("Phiên đăng nhập không hợp lệ");


            Object expiredAt = request.getSession().getAttribute("expiredAt");
            if (DateTimeHelper.strToLocalDateTime(expiredAt.toString()).isBefore(LocalDateTime.now()))
                throw new AppException("Phiên đăng nhập hết hạn");

            return true;
        } catch (NullPointerException | NumberFormatException | AppException e) {
            request.getSession().removeAttribute("librarianId");
            request.getSession().removeAttribute("expiredAt");
            log.info("[ERROR] Interceptor.preHandle: {}", e.getMessage());

            response.sendRedirect("/login");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
