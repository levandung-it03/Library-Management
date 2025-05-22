package com.homework.library_management.repositories;

import com.homework.library_management.entities.BorrowingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRequestRepository extends JpaRepository<BorrowingRequest, Long> {

    @Query("""
        SELECT COUNT(br) > 0 FROM BorrowingRequest br
        WHERE br.membershipCard.membershipCard = :membershipCard AND br.returningStatus = 0
    """)
    boolean hasMembershipCardNotReturnYet(String membershipCard);

    @Query("SELECT br FROM BorrowingRequest br WHERE br.membershipCard = :membershipCard AND br.returningStatus = 0")
    Optional<BorrowingRequest> findCurrentBorrowingRequestOfMembership(String membershipCard);

}
