package com.company.adminapi.dto;

/**
 * 在线会话
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 在线会话
 */
@Data
public class OnlineSession implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 会话编号（即 token）
	 */
	private String tokenId;

	/**
	 * 用户id
	 */
	private Integer sysUserId;

	/**
	 * 账号
	 */
	private String account;

	/**
	 * 登录ip
	 */
	private String ipaddr;

	/**
	 * 所属部门名称
	 */
	private String deptName;

	/**
	 * 登录时间
	 */
	private LocalDateTime loginTime;
}
