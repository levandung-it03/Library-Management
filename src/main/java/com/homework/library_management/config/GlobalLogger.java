package com.homework.library_management.config;

import com.homework.library_management.entities.log.AppLog;
import com.homework.library_management.entities.common.LogStorage;
import com.homework.library_management.entities.log.AppTestLog;
import com.homework.library_management.enums.Loggers;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.AppLogRepository;
import com.homework.library_management.repositories.AppTestLogRepository;
import com.homework.library_management.utils.APIHelper;
import com.homework.library_management.utils.DateTimeHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class GlobalLogger {
    public static String GROUP_NAME = "currentGroupLogId";
    public static String KEEP_LOGGING_NAME = "keepLogging";
    LogStorage logStorage;
    AppLogRepository appLogRepository;
    AppTestLogRepository appTestLogRepository;

    public void success(HttpServletRequest request, String message, Object... args) {
        this.info(request, message, Loggers.SUCCESS, args);
    }

    public void weird(HttpServletRequest request, String message, Object... args) {
        this.info(request, message, Loggers.WEIRD, args);
    }

    public void handling(HttpServletRequest request, String message, Object... args) {
        this.info(request, message, Loggers.HANDLING, args);
    }

    public void error(HttpServletRequest request, String message, Object... args) {
        this.info(request, message, Loggers.ERROR, args);
    }

    public void request(HttpServletRequest request, String message, Object... args) {
        this.info(request, message, Loggers.REQUEST, args);
    }

    public void steps(HttpServletRequest request, String message, Object... args) {
        this.info(request, message, Loggers.STEPS, args);
    }

    public void info(HttpServletRequest request, String message, Loggers type, Object... args) {
        var currentGroupLogId = request.getSession().getAttribute(GROUP_NAME);
        var mainMsg = "[" + type.toString() + "]" + String.format(message, args);
        log.info(mainMsg);
        try {
            logStorage.put(AppLog.builder()
                .timeAsId(DateTimeHelper.localDateMilliTimeToStr(LocalDateTime.now()))
                .groupId(currentGroupLogId.toString())
                .type(type.toString())
                .msg(String.format(message, args))
                .build());
        } catch (NullPointerException e) {
            this.logErrorAndClearSession(request);
        }
    }

    public void startLogging(HttpServletRequest request) {
        var newGroupLogId = UUID.randomUUID().toString();
        logStorage.init(newGroupLogId);
        //--Remove the previous group if .saveLog hasn't called yet
        request.getSession().removeAttribute(GROUP_NAME);
        request.getSession().setAttribute(GROUP_NAME, newGroupLogId);
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public Map saveLogs(HttpServletRequest request, String testId) throws AppException {
        try {
            if (Objects.isNull(request.getSession().getAttribute(GROUP_NAME)))
                return Map.of(
                    "rootLog", AppLog.builder().msg("Đã nhận tín hiệu log nhưng không có gì để lưu").build(),
                    "logs", List.of()
                );
            var currentGroupLogId = request.getSession().getAttribute(GROUP_NAME).toString();
            var logs = logStorage.getGroupById(currentGroupLogId);
            if(Objects.isNull(logs) || logs.isEmpty())
                return Map.of(
                    "rootLog", AppLog.builder().msg("Đã nhận tín hiệu log nhưng không có gì để lưu").build(),
                    "logs", List.of()
                );
            var savedLogs = appLogRepository.saveAll(logs);
            var rootLog = appTestLogRepository.save(AppTestLog.builder().groupId(currentGroupLogId).testId(testId).build());

            log.info("[{}]_Logs saved at {}", Loggers.INFO, DateTimeHelper.localDateTimeToStr(LocalDateTime.now()));
            logStorage.clearGroup(currentGroupLogId);
            return Map.of(
                "rootLog", rootLog,
                "logs", savedLogs
            );
        } catch (Exception e) {
            this.logErrorAndClearSession(request);
        }
        return Map.of();
    }

    private void logErrorAndClearSession(HttpServletRequest request) throws AppException {
        log.error("An error occurred when saving logs into server");
        APIHelper.clearSession(request);
        throw new AppException("Có lỗi server khi ghi log, vui lòng đăng xuất!_/login");
    }

    public void markRequestToContinueLoggingWhenRedirect(HttpServletRequest request) {
        request.getSession().setAttribute(KEEP_LOGGING_NAME, 1);
    }
}
