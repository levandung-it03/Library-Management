package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.services.MembershipCardService;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.homework.library_management.enums.StatusMsgEnum.SUCCESS;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipCardController {
    GlobalLogger logger;
    MembershipCardService membershipCardService;

    @GetMapping("/membership-card-list")
    public String renderMembershipCardList(
        @RequestParam(name = "currentPage", required = false, defaultValue = "1") int page,
        @RequestParam(name = "query", required = false, defaultValue = "") String query,
        HttpServletRequest request) {
        logger.handling(request, "MembershipCardController.renderMembershipCardList");
        membershipCardService.prepareMembershipCardList(request, page, query);
        logger.success(request, "`renderMembershipCardList` successfully");
        return "membership_list";
    }

    @PostMapping("/toggle-membership-status")
    public String toggleCardStatus(HttpServletRequest request,
        @NotEmpty(message = "Thẻ thư viện không được rỗng_/borrowing-book")
        @NotNull(message = "Thẻ thư viện không đúng_/borrowing-book")
        @Length(min = 6, max = 6, message = "Thẻ thư viện không hợp lệ_/borrowing-book")
        @RequestParam(name = "membershipCard") String membershipCard) {
        logger.handling(request, "MembershipCardController.toggleCardStatus");
        membershipCardService.toggleMembershipStatus(request, membershipCard);
        var successMsg = "Thay đổi thành công";
        logger.success(request, successMsg);
        logger.markRequestToContinueLoggingWhenRedirect(request);
        return "redirect:/membership-card-list?" + SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg(successMsg);
    }

}
