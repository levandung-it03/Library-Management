package com.homework.library_management.dto;

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
    @NotEmpty(message = "Id không hợp lệ")
    String id;
}
