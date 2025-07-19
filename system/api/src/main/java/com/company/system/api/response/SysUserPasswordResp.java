package com.company.system.api.response;

import lombok.Data;

@Data
public class SysUserPasswordResp {
	/**
	 * 密码是否可用?
	 */
	private Boolean canUse;

	/**
	 * 密码（canUse=true有值）
	 */
	private String password;

	/**
	 * 密码提醒
	 */
	private String passwordTips;
}