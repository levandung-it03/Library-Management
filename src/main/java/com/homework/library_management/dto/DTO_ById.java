package com.homework.library_management.dto;

import com.homework.library_management.dto.annotation.NotEmptyConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_ById {

    @NotNull(message = "Id không hợp lệ")
    @NotEmptyConstraint(message = "Id không được rỗng, hoặc chỉ chứa khoảng trắng")
    String id;
}
