package com.company.adminapi.amqp.strategy.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SysOperLogDto {
	/**
	 * sys_user.id
	 */
	private Integer sysUserId;
	
	/**
	 * 模块标题
	 */
	private String title;

	/**
	 * 业务类型(0其它 1新增 2修改 3删除)
	 */
	private Integer businessType;

	/**
	 * 方法名称
	 */
	private String method;

	/**
	 * 请求方式
	 */
	private String requestMethod;

	/**
	 * 请求URL
	 */
	private String operUrl;

	/**
	 * 主机地址
	 */
	private String operIp;

	/**
	 * 请求参数
	 */
	private String operParam;

	/**
	 * 返回参数
	 */
	private String jsonResult;

	/**
	 * 操作状态(0正常 1异常)
	 */
	private Integer status;

	/**
	 * 错误消息
	 */
	private String errorMsg;

	/**
	 * 耗时(毫秒)
	 */
	private Integer costTime;

	/**
	 * 操作时间
	 */
	private LocalDateTime operTime;
}