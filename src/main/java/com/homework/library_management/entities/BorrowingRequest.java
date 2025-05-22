package com.homework.library_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "borrowing_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "borrowing_request_id")
    Long borrowingRequestId;

    @ManyToOne
    @JoinColumn(name = "librarian_id", referencedColumnName = "librarian_id", nullable = false)
    Librarian librarian;

    @ManyToOne
    @JoinColumn(name = "membership_card", referencedColumnName = "membership_card", nullable = false)
    MembershipCard membershipCard;

    @NotNull
	@Column(name = "borrowing_time")
	@Temporal(TemporalType.TIMESTAMP)
    LocalDateTime borrowingTime;
    
	@Column(name = "returned_time")
	@Temporal(TemporalType.TIMESTAMP)
    LocalDateTime returnedTime;
	
	@Column(name = "returning_status")
    @Min(0)
    @Max(1)
    Integer returningStatus;
	
	@OneToMany(
        mappedBy = "borrowingRequest",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
	List<BookBorrowingRequest> bookBorrowingRequests;
}
