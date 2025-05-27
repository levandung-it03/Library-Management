package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
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
    GlobalLogger logger;
    BorrowingRequestService borrowingRequestService;

    @GetMapping("/borrowing-book")
    public String renderBorrowingBook(
        @RequestParam(name = "currentPage", required = false, defaultValue = "1") int page,
        @RequestParam(name = "query", required = false, defaultValue = "") String query,
        @RequestParam(name = "returningMembershipCard", required = false, defaultValue = "") String membershipCard,
        HttpServletRequest request) {
        try {
            logger.handling(request, "BorrowingRequestController.renderBorrowingBook");
            borrowingRequestService.prepareBorrowingBook(request, page, query, membershipCard);
            logger.success(request, "`renderBorrowingBook` successfully");
        } catch (AppException e) {
            logger.error(request, "AppException: %s", e.getMessage());
            request.setAttribute(ERROR.getMsg(), e.getMessage());
        }
        return "book_borrowing";
    }

    @PostMapping("/create-borrowing-request")
    public String createBorrowingRequest(@Valid DTO_BorrowingBook dto, HttpServletRequest request) throws AppException {
        logger.handling(request, "BorrowingRequestController.createBorrowingRequest");
        borrowingRequestService.createBorrowingRequest(request, dto);
        var successMsg = "Cho mượn thành công";
        logger.success(request, successMsg);
        logger.markRequestToContinueLoggingWhenRedirect(request);
        return "redirect:/borrowing-book?" + SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg(successMsg);
    }

    @PostMapping("/return-books")
    public String createReturningRequest(@Valid DTO_AboutMembershipCard dto, HttpServletRequest request)
        throws AppException {
        logger.handling(request, "BorrowingRequestController.createReturningRequest");
        borrowingRequestService.createReturningRequest(request, dto.getMembershipCard());
        var successMsg = "Trả sách thành công";
        logger.success(request, successMsg);
        logger.markRequestToContinueLoggingWhenRedirect(request);
        return "redirect:/borrowing-book?" + SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg(successMsg);
    }

    @ResponseBody
    @GetMapping("/get-borrowing-history")
    public String renderBorrowingHistory(@Valid DTO_AboutMembershipCard dto, HttpServletRequest request) {
        logger.handling(request, "BorrowingRequestController.renderBorrowingHistory");
        var result = borrowingRequestService.getBorrowingHistory(request, dto.getMembershipCard());
        logger.success(request, "`renderBorrowingHistory` successfully");
        return result;
    }
}
