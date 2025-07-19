package com.company.system.api.request;

import lombok.Data;

@Data
public class SaveNewPasswordReq {
	private Integer sysUserId;
	private String password;
}