package com.homework.library_management.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum StatusMsgEnum {
	ERROR("ERROR"),
	SUCCESS("SUCCESS")
	;
	
	String msg;
	StatusMsgEnum(String msg) {
		this.msg = msg;
	}

	public static ResponseEntity<Map> fastError(String msg) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR.getMsg(), msg));
	}

	public static ResponseEntity<Map> fastSuccess(String msg) {
		return ResponseEntity.ok(Map.of(SUCCESS.getMsg(), msg));
	}
}
