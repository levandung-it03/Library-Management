package com.homework.library_management.dto;

import java.util.List;

import com.homework.library_management.dto.annotation.NotEmptyConstraint;
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
public class DTO_BorrowingBook {
	@NotEmptyConstraint(message = "Thẻ thư viện không được rỗng, hoặc chỉ chứa khoảng trắng_/borrowing-book")
	@NotNull(message = "Thẻ thư viện không đúng_/borrowing-book")
	@Length(min = 6, max = 6, message = "Thẻ thư viện không hợp lệ_/borrowing-book")
    String membershipCard;
	
	@NotEmpty(message = "Danh sách sách mượn không được rỗng_/borrowing-book")
	@NotNull(message = "Danh sách sách mượn không hợp lệ_/borrowing-book")
	List<String> borrowedBooks;
}
