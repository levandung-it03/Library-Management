package com.homework.library_management.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "membership_card")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipCard {
    @Id
    @Column(name = "membership_card", length = 6)
    String membershipCard;

    @Column(name = "prohibited", columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    @Min(0)
    @Max(1)
    Integer prohibited;
}
