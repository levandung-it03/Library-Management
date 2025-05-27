package com.homework.library_management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_UpdateFullName {
    @Length(min = 1, max = 50, message = "Tên quá dài, hoặc quá ngắn_/user-info")
    @NotNull(message = "Tên không hợp lệ_/user-info")
    String fullName;
}
