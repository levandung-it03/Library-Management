package com.homework.library_management.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_AboutMembershipCard {
    @NotNull(message = "Thẻ thư viện không hợp lệ_/membership-card-list")
    @NotEmpty(message = "Thẻ thư viện không được trống_/membership-card-list")
    @Length(min = 6, max = 6, message = "Thẻ thư viện không hợp lệ_/membership-card-list")
    String membershipCard;
}
