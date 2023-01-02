package com.company.auth.authentication.impl.tool.dto;

import lombok.Data;

@Data
public class AliMobile {
	private boolean success;
	private String msg;
	
	private String mobile;
}
