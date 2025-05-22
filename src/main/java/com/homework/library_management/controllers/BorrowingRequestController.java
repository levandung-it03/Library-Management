package com.homework.library_management.controllers;

import com.homework.library_management.dto.DTO_AboutMembershipCard;
import com.homework.library_management.dto.DTO_BorrowingBook;

import static com.homework.library_management.enums.StatusMsgEnum.*;

import com.homework.library_management.exception.AppException;
import com.homework.library_management.services.BorrowingRequestService;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowingRequestController {
    BorrowingRequestService borrowingRequestService;

    @GetMapping("/borrowing-book")
    public String borrowingBook(
        @RequestParam(name = "currentPage", required = false, defaultValue = "1") int page,
        @RequestParam(name = "query", required = false, defaultValue = "") String query,
        @RequestParam(name = "returningMembershipCard", required = false, defaultValue = "") String membershipCard,
        HttpServletRequest request) {
        try {
            borrowingRequestService.prepareBorrowingBook(request, page, query, membershipCard);
        } catch (AppException e) {
            request.setAttribute(ERROR.getMsg(), e.getMessage());
        }
        return "book_borrowing";
    }

    @PostMapping("/create-borrowing-request")
    public String createBorrowingRequest(@Valid DTO_BorrowingBook dto, HttpServletRequest request) throws AppException {
        borrowingRequestService.createBorrowingRequest(request, dto);
        return "redirect:/borrowing-book?" + SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg("Cho mượn thành công");
    }

    @PostMapping("/return-books")
    public String createBorrowingRequest(@Valid DTO_AboutMembershipCard dto) throws AppException {
        borrowingRequestService.createReturningRequest(dto.getMembershipCard());
        return "redirect:/borrowing-book?" + SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg("Trả sách thành công");
    }

    @ResponseBody
    @GetMapping("/get-borrowing-history")
    public String getBorrowingHistory(@Valid DTO_AboutMembershipCard dto) {
        return borrowingRequestService.getBorrowingHistory(dto.getMembershipCard());
    }
}
