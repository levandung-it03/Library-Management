package com.homework.library_management.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_SendOTPSucceededRes {
    String SUCCESS;
    int otpAge;

    @Override
    public String toString() {
        return String.format("{\"SUCCESS\":\"%s\", \"otpAge\":%s}", SUCCESS, otpAge);
    }
}
