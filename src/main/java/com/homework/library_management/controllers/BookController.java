package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_AddBookReq;
import com.homework.library_management.dto.DTO_UpdateBookReq;
import com.homework.library_management.enums.StatusMsgEnum;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.services.BookService;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    GlobalLogger logger;
    BookService bookService;

    @GetMapping("/manage-book")
    public String renderManageBook(
        @RequestParam(name = "currentPage", required = false, defaultValue = "1") int page,
        @RequestParam(name = "query", required = false, defaultValue = "") String query,
        HttpServletRequest request) {
        logger.handling(request, "BookController.renderManageBook");
        bookService.prepareManageBook(request, page, query);
        logger.success(request, "`renderManageBook` successfully");
        return "book_manage";
    }

    @GetMapping("/detail-book")
    public String renderDetailBook(@RequestParam("id") @NotNull Long id, HttpServletRequest request) {
        try {
            logger.handling(request, "BookController.renderDetailBook");
            bookService.showDetailBook(request, id);
            logger.success(request, "`renderDetailBook` successfully");
            return "book_detail";
        } catch (AppException e) {
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/manage-book?" + StatusMsgEnum.ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(e.getMessage());
        }
    }

    @PostMapping("/add-book")
    public String addBook(@Valid DTO_AddBookReq dto, HttpServletRequest request) {
        try {
            logger.handling(request, "BookController.addBook");
            bookService.addBook(request, dto);
            var successMsg = "Thêm sách thành công";
            logger.success(request, successMsg);
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/manage-book?" + StatusMsgEnum.SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg(successMsg);
        } catch (AppException e) {
            logger.error(request, "AppException: %s", e.getMessage());
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/manage-book?" + StatusMsgEnum.ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(e.getMessage());
        }
    }

    @PostMapping("/update-book")
    public String updateBook(@Valid DTO_UpdateBookReq dto, HttpServletRequest request) {
        try {
            logger.handling(request, "BookController.updateBook");
            bookService.updateBook(request, dto);
            var successMsg = "Cập nhật sách thành công";
            logger.success(request, successMsg);
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/manage-book?" + StatusMsgEnum.SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg(successMsg);
        } catch (AppException e) {
            logger.error(request, "AppException: %s", e.getMessage());
            logger.markRequestToContinueLoggingWhenRedirect(request);
            return "redirect:/manage-book?" + StatusMsgEnum.ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(e.getMessage());
        }
    }
}
