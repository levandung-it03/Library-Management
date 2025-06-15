package com.homework.library_management.dto;

import com.homework.library_management.dto.annotation.NotEmptyConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_VerifyEmail {
    @NotEmpty(message = "Email không được trống")
    @NotNull(message = "Email không hợp lệ")
    @Length(max = 30, message = "Email quá dài")
    @Email(message = "Sai email")
    String email;

    @NotEmptyConstraint(message = "Option không được rỗng, hoặc chỉ chứa khoảng trắng")
    @NotNull(message = "Option không được trống")
    String option;
}
