package com.homework.library_management.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "librarian")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Librarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "librarian_id")
    Long librarianId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    Account account;

	@Column(name = "full_name")
    String fullName;
	
	@Column(name = "employee_id")
    String employeeId;

}
