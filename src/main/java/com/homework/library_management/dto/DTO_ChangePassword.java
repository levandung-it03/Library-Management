package com.homework.library_management.dto;

import jakarta.validation.constraints.Email;
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
public class DTO_ChangePassword {

    @NotEmpty(message = "Email không được trống_/login")
    @NotNull(message = "Email không hợp lệ_/login")
    @Length(max = 30, message = "Email quá dài_/login")
    @Email(message = "Sai email_/login")
    String email;

    @NotEmpty(message = "Mật khẩu không được trống_/login")
    @NotNull(message = "Mật khẩu không hợp lệ_/login")
    @Length(max = 30, message = "Mật khẩu quá dài_/login")
    String newPassword;

    @NotEmpty(message = "Otp không được trống")
    @NotNull(message = "Otp không được trống")
    String otp;
}
