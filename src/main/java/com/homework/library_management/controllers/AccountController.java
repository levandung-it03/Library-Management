package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_ById;

import com.homework.library_management.enums.StatusMsgEnum;
import com.homework.library_management.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    GlobalLogger logger;

    @ResponseBody
    @PostMapping("/save-log")
    public ResponseEntity<Map> saveLog(HttpServletRequest request, @Valid @RequestBody DTO_ById dto) {
        try {
            return ResponseEntity.ok(Map.of(
                StatusMsgEnum.SUCCESS.getMsg(), "Lưu thành công các logs",
                "logs", logger.saveLogs(request, dto.getId())));
        } catch (AppException e) {
            return ResponseEntity.badRequest().body(Map.of(StatusMsgEnum.ERROR.getMsg(), e.getMessage()));
        }
    }

}
