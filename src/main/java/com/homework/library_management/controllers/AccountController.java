package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_ById;
import com.homework.library_management.dto.DTO_ChangePassword;
import com.homework.library_management.dto.DTO_GenerateForgotPassword;
import com.homework.library_management.dto.DTO_VerifyEmail;

import com.homework.library_management.enums.StatusMsgEnum;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.services.AccountService;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    GlobalLogger logger;
    AccountService accountService;

    @ResponseBody
    @PostMapping("/save-log")
    public ResponseEntity<Map> saveLog(HttpServletRequest request, @Valid @RequestBody DTO_ById dto) {
        try {
            logger.saveLogs(request, dto.getId());
            return ResponseEntity.ok(Map.of(StatusMsgEnum.SUCCESS.getMsg(), "Lưu thành công các logs"));
        } catch (AppException e) {
            return ResponseEntity.badRequest().body(Map.of(StatusMsgEnum.ERROR.getMsg(), e.getMessage()));
        }
    }

    @ResponseBody
    @PostMapping("/verify-email")
    public ResponseEntity<Map> verifyEmail(HttpServletRequest request, @Valid @RequestBody DTO_VerifyEmail dto) {
        try {
            logger.handling(request, "AccountController.verifyEmail");
            var result = accountService.getOtpToVerifyEmail(request, dto);
            logger.handling(request, "`verifyEmail` successfully for " + dto.getOption());
            return result;
        } catch (AppException e) {
            return StatusMsgEnum.fastError(e.getMessage());
        }
    }

    @GetMapping("/forgot-password")
    public String renderForgotPasswordPage(HttpServletRequest request) {
        logger.handling(request, "AccountController.renderForgotPasswordPage");
        logger.success(request, "`renderForgotPasswordPage` successfully");
        return "forgot_password";
    }

    @ResponseBody
    @PostMapping("/generate-password")
    public ResponseEntity<Map> generateNewPassword(HttpServletRequest request,
                                                   @Valid @RequestBody DTO_GenerateForgotPassword dto) {
        try {
            logger.handling(request, "AccountController.generateNewPassword");
            var result = accountService.generatePasswordForUserForgot(request, dto);
            logger.success(request, "`generateNewPassword` successfully");
            return result;
        } catch (AppException e) {
            return StatusMsgEnum.fastError(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid DTO_ChangePassword dto, HttpServletRequest request) throws AppException {
        try {
            logger.handling(request, "AccountController.changePassword");
            accountService.changePassword(dto, request);
            APIHelper.clearSession(request);
            logger.handling(request, "AccountController.clearSession");
            logger.success(request, "`changePassword` successfully");
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/login";
        } catch (AppException e) {
            logger.error(request, "AppException: %s", e.getMessage());
            String[] extractedErr = e.getMessage().split("_");
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:" + extractedErr[1] + "?" + StatusMsgEnum.ERROR.getMsg() + "="
                + APIHelper.encodeUrlMsg( extractedErr[0]);
        }
    }
}
