package com.company.auth.authentication.impl.tool.dto;

import lombok.Data;

@Data
public class MaSession {
	private Integer errcode;
	
	private String errmsg;
	
	private String sessionKey;

	private String openid;

	private String unionid;
	
	private String phoneNumber;
}
