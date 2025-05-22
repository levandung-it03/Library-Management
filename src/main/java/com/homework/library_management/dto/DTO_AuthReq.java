package com.homework.library_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class DTO_AuthReq {
	@NotEmpty(message = "Email không được trống_/login")
	@NotNull(message = "Email không hợp lệ_/login")
	@Length(max = 30, message = "Email quá dài_/login")
	@Email(message = "Sai email_/login")
	String email;

	@NotEmpty(message = "Mật khẩu không được trống_/login")
	@NotNull(message = "Mật khẩu không hợp lệ_/login")
	@Length(max = 30, message = "Mật khẩu quá dài_/login")
	String password;
}
