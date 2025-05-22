package com.homework.library_management.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

@Entity
@Table(name = "book_borrowing_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookBorrowingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_borrowing_request_id")
    Long bookBorrowingRequestId;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false)
    Book book;

    @ManyToOne
    @JoinColumn(name = "borrowing_request_id", referencedColumnName = "borrowing_request_id", nullable = false)
    BorrowingRequest borrowingRequest;

    @Min(1)
    @Max(999)
    @Column(name = "quantity")
    Integer quantity;

}
