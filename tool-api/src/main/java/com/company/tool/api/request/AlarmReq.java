package com.company.tool.api.request;

import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AlarmReq {
	/**
	 * Type
	 */
	@NotNull
	String type;

	/**
	 * 参数
	 */
	Map<String, String> templateParamMap;
}
