package com.homework.library_management.repositories;

import com.homework.library_management.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT SUM(b.availableQuantity) FROM Book b")
    Long countTotalCurrentBooks();

    @Query("SELECT SUM(b.availableQuantity) FROM Book b WHERE b.status = 1")
    Long countTotalAvailableBooks();

    @Query("SELECT SUM(b.availableQuantity) FROM Book b WHERE b.status = 0")
    Long countTotalLockedBooks();

    @Query("SELECT b FROM Book b WHERE b.bookName LIKE CONCAT('%', :bookName,'%')")
    Page<Book> findAllByBookName(String bookName, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.bookName LIKE CONCAT('%', :bookName,'%') AND b.status = 1")
    Page<Book> findAllAvailableBooksByBookName(String bookName, Pageable pageable);
}
