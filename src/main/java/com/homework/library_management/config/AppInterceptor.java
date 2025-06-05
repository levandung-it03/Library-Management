package com.homework.library_management.config;

import com.homework.library_management.entities.common.LogStorage;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.LibrarianRepository;
import com.homework.library_management.utils.APIHelper;
import com.homework.library_management.utils.DateTimeHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppInterceptor implements HandlerInterceptor {
    LogStorage logStorage;
    GlobalLogger logger;
    LibrarianRepository librarianRepository;
    String[] PASSED_PATH = {
        "/authenticate",
        "/login",
        "/forgot-password",
        "/generate-password",
        "/verify-email",
        "/favicon.ico",
        "/error",    //--Default from Spring
    };

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        if (request.getRequestURI().equals("/save-log"))
            return true;
        Object isKeepLogging = request.getSession().getAttribute(GlobalLogger.KEEP_LOGGING_NAME);
        if (!Objects.isNull(isKeepLogging) && isKeepLogging.equals(1)) {    //--Pass the redirected-request by Servlet
            logger.steps(request, "Handle REDIRECTING request: %s", request.getRequestURI());
            request.getSession().removeAttribute(GlobalLogger.KEEP_LOGGING_NAME);
            return true;
        }

        try {
            logger.startLogging(request);
            logger.request(request, "AppInterceptor.preHandle: URL %s, method %s",
                request.getRequestURI(), request.getMethod());

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

            logger.handling(request, "AppInterceptor.preHandle: LibrarianID %s, ClientIP %s", librarianId, clientIp);
            return true;
        } catch (NullPointerException | NumberFormatException | AppException e) {
            APIHelper.clearSession(request);
            logger.error(request, "%s: %s", e.getClass().toString(), e.getMessage());
        }
        response.sendRedirect("/login");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
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
        if (Objects.isNull(request.getSession().getAttribute(GlobalLogger.KEEP_LOGGING_NAME))) {
            System.out.println("FULL LOGGING FROM GROUP_ID: "
                + request.getSession().getAttribute(GlobalLogger.GROUP_NAME));
            logStorage
                .getGroupById(request.getSession().getAttribute(GlobalLogger.GROUP_NAME).toString())
                .forEach(System.out::println);

        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
