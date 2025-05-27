package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_AuthReq;

import static com.homework.library_management.enums.StatusMsgEnum.*;

import com.homework.library_management.exception.AppException;
import com.homework.library_management.services.AuthenticationService;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateController {
    GlobalLogger logger;
    AuthenticationService authenticationService;

    @PostMapping("/log-out")
    public String logout(HttpServletRequest request) {
        logger.handling(request, "AuthenticateController.logout");
        APIHelper.clearSession(request);
        logger.success(request, "`logout` successfully");
        logger.markRequestToContinueLoggingWhenRedirect(request);
        return "redirect:/login";
    }

    @PostMapping("/authenticate")
    public String authenticate(@Valid DTO_AuthReq dto, HttpServletRequest request) {
        try {
            logger.handling(request, "AuthenticateController.authenticate");
            authenticationService.authenticate(dto, request);
            logger.success(request, "`authenticate` successfully");
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/home";
        } catch (AppException exc) {
            logger.error(request, "AppException: %s", exc.getMessage());
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/login?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(exc.getMessage());
        }
    }

    @GetMapping("/login")
    public String renderLoginPage(@RequestParam(value = "ERROR", required = false) String errorMessage,
                            HttpServletRequest request) {
        logger.handling(request, "AuthenticateController.renderLoginPage");
        if (request.getSession().getAttribute("librarianId") != null) {
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/home";
        }
        if (errorMessage != null)
            request.setAttribute("errorMessage", errorMessage);
        logger.success(request, "`renderLoginPage` successfully");
        return "login";
    }
}
