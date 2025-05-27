package com.homework.library_management.repositories;

import com.homework.library_management.entities.log.AppLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppLogRepository extends JpaRepository<AppLog, String> {
}
