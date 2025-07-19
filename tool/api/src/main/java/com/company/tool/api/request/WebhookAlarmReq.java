package com.company.tool.api.request;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.company.tool.api.enums.WebhookEnum;

import lombok.Data;

@Data
public class WebhookAlarmReq {
	/**
	 * WebhookEnum.Type
	 */
	@NotNull
	WebhookEnum.Type type;

	/**
	 * 建议添加参数：时间（time）、主机（host）、日志ID（traceid）、应用（application），方便快速定位
	 */
	Map<String, String> templateParamMap;
}
