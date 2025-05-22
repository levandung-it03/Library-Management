package com.homework.library_management.repositories;

import com.homework.library_management.entities.MembershipCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipCardRepository extends JpaRepository<MembershipCard, String> {

    @Query("SELECT c FROM MembershipCard c WHERE c.membershipCard LIKE CONCAT('%', :card, '%')")
    Page<MembershipCard> findAllByMembershipCard(@Param("card") String card, Pageable pageable);

    @Query("""
        SELECT CASE WHEN EXISTS (
            SELECT 1 FROM MembershipCard c WHERE c.membershipCard = :card AND c.prohibited = 0
        ) THEN true ELSE false END
    """)
    boolean isValidMembershipCard(@Param("card") String card);
}
