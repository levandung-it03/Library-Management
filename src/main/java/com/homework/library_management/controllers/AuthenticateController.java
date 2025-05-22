package com.homework.library_management.controllers;

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
    AuthenticationService authenticationService;

    @PostMapping("/log-out")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("librarianId");
        request.getSession().removeAttribute("expiredAt");
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @PostMapping("/authenticate")
    public String authenticate(@Valid DTO_AuthReq dto, HttpServletRequest request) {
        try {
            authenticationService.authenticate(dto, request);
            return "redirect:/home";
        } catch (AppException exc) {
            return "redirect:/login?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(exc.getMessage());
        }
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "ERROR", required = false) String errorMessage,
                               HttpServletRequest request) {
        if (errorMessage != null)
            request.setAttribute("errorMessage", errorMessage);
        return "login";
    }
}
