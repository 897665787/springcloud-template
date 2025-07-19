package com.company.tool.api.request;

import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.company.tool.api.enums.EmailEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailReq {
	@NotBlank
	String email;
	@NotNull
	Map<String, String> templateParamMap;
	@NotNull
	EmailEnum.Type type;

	LocalDateTime planSendTime;
	LocalDateTime overTime;
}
