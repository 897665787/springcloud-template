package com.company.system.api.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 修改密码请求
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/
@Data
public class UpdatePasswordReq {

	/**
	 * 旧密码（明文）
	 */
	@NotBlank(message = "旧密码不能为空")
	private String oldPassword;

	/**
	 * 新密码（明文）
	 */
	@NotBlank(message = "新密码不能为空")
	private String newPassword;
}
