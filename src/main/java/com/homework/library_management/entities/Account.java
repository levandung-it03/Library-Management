package com.homework.library_management.entities;

import java.time.LocalDateTime;

import com.homework.library_management.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
    Long accountId;

	@Column(name = "email")
    String email;

	@Column(name = "password")
    String password;

	@Column(name = "active")
    Boolean active;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
    LocalDateTime createdTime;

    @Enumerated(EnumType.STRING)
	@Column(name = "role")
    RoleEnum role;
}
