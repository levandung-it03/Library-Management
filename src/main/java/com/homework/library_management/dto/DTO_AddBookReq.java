package com.homework.library_management.dto;

import com.homework.library_management.dto.annotation.NotEmptyConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @NotEmptyConstraint(message = "Tên sách không được rỗng, hoặc chỉ chứa khoảng trắng_/manage-book")
    @NotNull(message = "Tên sách không hợp lệ_/manage-book")
    String bookName;

    @NotEmptyConstraint(message = "Tên các tác giả không được rỗng, hoặc chỉ chứa khoảng trắng_/manage-book")
    @NotNull(message = "Tên các tác giả không hợp lệ_/manage-book")
    String authors;

    @NotNull(message = "Số lượng sách không hợp lệ_/manage-book")
    @Min(value = 1, message = "Số lượng sách không hợp lệ_/manage-book")
    Integer availableQuantity;

    @NotEmptyConstraint(message = "Mô tả không được rỗng, hoặc chỉ chứa khoảng trắng_/manage-book")
    @NotNull(message = "Mô tả không hợp lệ_/manage-book")
    String description;
    
//    String imgUrl;

    @NotNull(message = "Thể loại sách không được rỗng_/manage-book")
    @NotEmpty(message = "Thể loại sách không được rỗng_/manage-book")
    List<Long> genres;

}
