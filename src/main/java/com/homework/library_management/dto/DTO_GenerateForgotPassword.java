package com.homework.library_management.dto;

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
public class DTO_GenerateForgotPassword {
    @NotEmpty(message = "Email không được trống")
    @NotNull(message = "Email không hợp lệ")
    @Length(max = 30, message = "Email quá dài")
    @Email(message = "Sai email")
    String email;

    @NotEmpty(message = "Otp không được trống")
    @NotNull(message = "Otp không được trống")
    String otp;
}
