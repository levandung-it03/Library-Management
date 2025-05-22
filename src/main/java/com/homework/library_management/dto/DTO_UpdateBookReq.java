package com.homework.library_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_UpdateBookReq {
	@NotNull(message = "Id sách không hợp lệ_/manage-book")
    Long bookId;

    @NotEmpty(message = "Tên sách không được rỗng_/manage-book")
    @NotNull(message = "Tên sách không hợp lệ_/manage-book")
    String bookName;

    @NotEmpty(message = "Tên các tác giả không được rỗng_/manage-book")
    @NotNull(message = "Tên các tác giả không hợp lệ_/manage-book")
    String authors;

	@NotNull(message = "Số lượng sách không hợp lệ_/manage-book")
    @Min(value = 1, message = "Số lượng sách không hợp lệ_/manage-book")
    Integer availableQuantity;

    @NotEmpty(message = "Mô tả không được rỗng_/manage-book")
    @NotNull(message = "Mô tả không hợp lệ_/manage-book")
    String description;
    
//    String imgUrl;
    @NotNull(message = "Trạng thái sách không hợp lệ_/manage-book")
    @Min(value = 0, message = "Trạng thái sách không hợp lệ_/manage-book")
    @Max(value = 1, message = "Trạng thái sách không hợp lệ_/manage-book")
    Integer status;

    @NotNull(message = "Thể loại không hợp lệ_/manage-book")
    @NotEmpty(message = "Thể loại không được bỏ trống_/manage-book")
    List<Long> genres;

}
