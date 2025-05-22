package com.homework.library_management.controllers;

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
    BookService bookService;

    @GetMapping("/manage-book")
    public String manageBook(
        @RequestParam(name = "currentPage", required = false, defaultValue = "1") int page,
        @RequestParam(name = "query", required = false, defaultValue = "") String query,
        HttpServletRequest request) {
        bookService.prepareManageBook(request, page, query);
        return "book_manage";
    }

    @GetMapping("/detail-book")
    public String detailBook(@RequestParam("id") @NotNull Long id, HttpServletRequest request) {
        try {
            bookService.showDetailBook(request, id);
            return "book_detail";
        } catch (AppException e) {
            return "redirect:/manage-book?" + StatusMsgEnum.ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(e.getMessage());
        }
    }

    @PostMapping("/add-book")
    public String addBook(@Valid DTO_AddBookReq request) {
        try {
            bookService.addBook(request);
            return "redirect:/manage-book?" + StatusMsgEnum.SUCCESS.getMsg() + "="
                + APIHelper.encodeUrlMsg("Thêm sách thành công");
        } catch (AppException e) {
            return "redirect:/manage-book?" + StatusMsgEnum.ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(e.getMessage());
        }
    }

    @PostMapping("/update-book")
    public String updateBook(@Valid DTO_UpdateBookReq request) {
        try {
            bookService.updateBook(request);
            return "redirect:/manage-book?" + StatusMsgEnum.SUCCESS.getMsg() + "="
                + APIHelper.encodeUrlMsg("Cập nhật sách thành công");
        } catch (AppException e) {
            return "redirect:/manage-book?" + StatusMsgEnum.ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(e.getMessage());
        }
    }
}
