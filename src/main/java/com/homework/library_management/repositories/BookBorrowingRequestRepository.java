package com.homework.library_management.repositories;

import com.homework.library_management.entities.BookBorrowingRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowingRequestRepository extends JpaRepository<BookBorrowingRequest, Long> {
    @Query("""
        SELECT SUM(bbr.quantity) FROM BookBorrowingRequest bbr
        WHERE bbr.borrowingRequest.returningStatus = 0
    """)
    Long countTotalBorrowedBooks();

    @Query("""
        SELECT bbr FROM BookBorrowingRequest bbr
        WHERE bbr.borrowingRequest.membershipCard.membershipCard = :membershipCard
        AND bbr.borrowingRequest.returningStatus = 0
    """)
    List<BookBorrowingRequest> findAllByBorrowingRequestThatNotReturnByMembershipCard(String membershipCard);

    @Query("""
        SELECT
            bbr.borrowingRequest.borrowingRequestId,
            bbr.borrowingRequest.borrowingTime,
            bbr.borrowingRequest.returnedTime,
            bbr.book.bookName,
            bbr.quantity
        FROM BookBorrowingRequest bbr
        WHERE bbr.borrowingRequest.membershipCard.membershipCard = :membershipCard
    """)
    List<Object[]> findAllHistoriesByMembershipCard(@Param("membershipCard") String membershipCard);

    @Query("""
        SELECT CASE WHEN EXISTS (
            SELECT 1 FROM BookBorrowingRequest bbr
            WHERE bbr.book.bookId = :bookId AND bbr.borrowingRequest.returningStatus = 0
        ) THEN true ELSE false END
    """)
    boolean existsByBookIdAndNotReturnYet(@Param("bookId") Long bookId);
}
