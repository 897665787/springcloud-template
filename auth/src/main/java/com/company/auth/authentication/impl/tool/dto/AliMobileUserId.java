package com.company.auth.authentication.impl.tool.dto;

import lombok.Data;

@Data
public class AliMobileUserId {
	private boolean success;
	private String msg;
	
	private String mobile;
	private String userId;
}
