package com.homework.library_management.controllers;

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
    MembershipCardService membershipCardService;

    @GetMapping("/membership-card-list")
    public String home(
        @RequestParam(name = "currentPage", required = false, defaultValue = "1") int page,
        @RequestParam(name = "query", required = false, defaultValue = "") String query,
        HttpServletRequest request) {
        membershipCardService.prepareMembershipCardList(request, page, query);
        return "membership_list";
    }

    @PostMapping("/toggle-membership-status")
    public String toggleStatus(
        @NotEmpty(message = "Thẻ thư viện không được rỗng_/borrowing-book")
        @NotNull(message = "Thẻ thư viện không đúng_/borrowing-book")
        @Length(min = 6, max = 6, message = "Thẻ thư viện không hợp lệ_/borrowing-book")
        @RequestParam(name = "membershipCard") String membershipCard) {
        membershipCardService.toggleMembershipStatus(membershipCard);
        return "redirect:/membership-card-list?" + SUCCESS.getMsg() + "=" + APIHelper.encodeUrlMsg("Thay đổi thành công");
    }

}
