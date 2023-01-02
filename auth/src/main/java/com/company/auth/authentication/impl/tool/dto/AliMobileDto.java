package com.company.auth.authentication.impl.tool.dto;

import lombok.Data;

@Data
public class AliMobileDto {
	private String code;
	private String msg;
	
	private String subCode;	
	private String subMsg;	
	
	private String mobile;
}
