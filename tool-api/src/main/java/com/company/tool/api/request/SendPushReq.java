package com.company.tool.api.request;

import com.company.tool.api.enums.PushEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendPushReq {
	@NotBlank
	String deviceid;
	@NotNull
	Map<String, String> templateParamMap;
	@NotNull
	PushEnum.Type type;

	LocalDateTime planSendTime;
	LocalDateTime overTime;
}
