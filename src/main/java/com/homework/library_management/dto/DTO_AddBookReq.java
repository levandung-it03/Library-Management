package com.homework.library_management.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_AddBookReq {
    @NotEmpty(message = "Tên sách không hợp lệ_/manage-book")
    @NotNull(message = "Tên sách không hợp lệ_/manage-book")
    String bookName;

    @NotEmpty(message = "Tên các tác giả không hợp lệ_/manage-book")
    @NotNull(message = "Tên các tác giả không hợp lệ_/manage-book")
    String authors;

	@NotNull(message = "Số lượng sách không hợp lệ_/manage-book")
    Integer availableQuantity;

    @NotEmpty(message = "Mô tả không hợp lệ_/manage-book")
    @NotNull(message = "Mô tả không hợp lệ_/manage-book")
    String description;
    
//    String imgUrl;

    @NotNull(message = "Thể loại sách không hợp lệ_/manage-book")
    @NotEmpty(message = "Thể loại sách không được rỗng_/manage-book")
    List<Long> genres;

}
