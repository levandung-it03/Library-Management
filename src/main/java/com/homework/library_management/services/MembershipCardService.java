package com.homework.library_management.services;

import com.homework.library_management.entities.MembershipCard;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.MembershipCardRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipCardService {
    MembershipCardRepository membershipCardRepository;
    LibrarianService librarianService;

    public void prepareMembershipCardList(HttpServletRequest request, int page, String query) {
        librarianService.attachLibrarianInfo(request);
        page = page < 0 ? 0 : page - 1;
        Page<MembershipCard> cards = membershipCardRepository.findAllByMembershipCard(query,
            PageRequest.of(page, 30));

        if (!query.isEmpty())
            request.setAttribute("query", query);
        request.setAttribute("currentPage", page + 1);
        request.setAttribute("totalPages", cards.getTotalPages());
        request.setAttribute("cards", cards.stream().toList());
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public void toggleMembershipStatus(String membershipCard) throws AppException {
        var updatedMembershipCard = membershipCardRepository.findById(membershipCard)
            .orElseThrow(() -> new AppException("Mã thẻ không tồn tại_/membership-card-list"));
        updatedMembershipCard.setProhibited(Math.abs(updatedMembershipCard.getProhibited() - 1));
        membershipCardRepository.save(updatedMembershipCard);
    }
}
