package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_UpdateFullName;
import com.homework.library_management.services.LibrarianService;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.homework.library_management.enums.StatusMsgEnum.SUCCESS;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LibrarianController {
    GlobalLogger logger;
    LibrarianService librarianService;

    @GetMapping("/user-info")
    public String renderUserInfoPage(HttpServletRequest request) {
        logger.handling(request, "LibrarianController.renderUserInfoPage");
        librarianService.prepareDataToShowUserInfoPage(request);
        logger.success(request, "`renderUserInfoPage` successfully");
        return "user_info";
    }

    @PostMapping("/update-fullname")
    public String updateLibrarianFullName(HttpServletRequest request, @Valid DTO_UpdateFullName dto) {
        logger.handling(request, "LibrarianController.updateLibrarianFullName");
        librarianService.updateLibrarianFullName(request, dto);
        var successMsg = "Sửa tên thành công";
        logger.success(request, successMsg);
        logger.markRequestToContinueLoggingWhenRedirect(request);
        return "redirect:/user-info?" + SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg(successMsg);
    }
}
