package com.company.tool.api.request;

import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.company.tool.api.enums.SmsEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendSmsReq {
	@NotBlank
	String mobile;
	@NotNull
	Map<String, String> templateParamMap;
	@NotNull
	SmsEnum.Type type;

	LocalDateTime planSendTime;
	LocalDateTime overTime;
}
