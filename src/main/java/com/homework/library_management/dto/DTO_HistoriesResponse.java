package com.homework.library_management.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTO_HistoriesResponse {
    Long borrowingRequestId;
    String borrowingTime;
    String returnedTime;
    List<DTO_BookOfHistoriesResponse> books;

    @Override
    public String toString() {
        String books = "[" +
            String.join(",", this.books.stream().map(DTO_BookOfHistoriesResponse::toString).toList()) +
            "]";
        return String.format("{\"borrowingRequestId\":%s,\"borrowingTime\":\"%s\",\"returnedTime\":%s,\"books\":%s}",
            borrowingRequestId, borrowingTime, Objects.isNull(returnedTime) ? null : "\"" + returnedTime + "\"", books);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DTO_BookOfHistoriesResponse {
        String bookName;
        Integer borrowedQuantity;

        @Override
        public String toString() {
            return String.format("{\"bookName\":\"%s\",\"borrowedQuantity\":%s}", bookName, borrowedQuantity);
        }
    }
}
