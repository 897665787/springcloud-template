package com.company.system.api.request;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 用户登录日志
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysLogininfoReq {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * sys_user.id
	 */
	private Integer sysUserId;

	/**
	 * 登录时间
	 */
	private LocalDateTime loginTime;

	/**
	 * 登录账户
	 */
	private String account;

	/**
	 * 设备(ADMIN:admin端)
	 */
	private String device;

	/**
	 * 平台
	 */
	private String platform;

	/**
	 * 操作系统
	 */
	private String operator;

	/**
	 * 版本
	 */
	private String version;

	/**
	 * 设备ID
	 */
	private String deviceid;

	/**
	 * 渠道
	 */
	private String channel;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 登录地区(根据ip获得)
	 */
	private String address;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 语言
	 */
	private String lang;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

}