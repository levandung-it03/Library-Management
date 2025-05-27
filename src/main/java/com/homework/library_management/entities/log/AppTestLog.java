package com.homework.library_management.entities.log;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "app_test_log",
    indexes = {
        @Index(name = "idx_group_id", columnList = "group_id"),
        @Index(name = "idx_test_id", columnList = "test_id"),
    }
)
public class AppTestLog {
    @Id
    Long logId;

    @Column(name = "group_id")
    String groupId;

    @Column(name = "test_id")
    String testId;
}
