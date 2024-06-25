package com.company.tool.email.dto;

import java.util.Map;

import lombok.Data;

@Data
public class EmailTemplateParam {
	private String email;
	private Map<String, String> templateParamMap;
}
