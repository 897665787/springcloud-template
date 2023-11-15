package com.company.tool.qrcode.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaWxaCode {
	boolean success;

	String message;

	byte[] bytes;
}
