package com.company.tool.push.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DeviceTemplateParam {
	private String deviceid;
	private Map<String, String> templateParamMap;
}
