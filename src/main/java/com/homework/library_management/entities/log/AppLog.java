package com.homework.library_management.entities.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "app_log")
public class AppLog {
    @Id
    @Column(name = "time_as_id")
    String timeAsId;

    @Column(name = "group_id", nullable = false)
    String groupId;

    String type;
    String msg;
}
