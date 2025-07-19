package com.company.tool.subscribe.dto;

import java.util.List;

import lombok.Data;

@Data
public class OpenidTemplateParam {
	private String openid;
	private String page;
	private List<String> valueList;
}
