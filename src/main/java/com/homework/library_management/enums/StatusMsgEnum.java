package com.homework.library_management.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

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
}
