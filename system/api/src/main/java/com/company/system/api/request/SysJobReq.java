package com.company.system.api.request;

/**
 * 定时任务请求
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import lombok.Data;

/**
 * 定时任务请求
 */
@Data
public class SysJobReq {

	/**
	 * 任务id
	 */
	private Integer id;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务组名
	 */
	private String jobGroup;

	/**
	 * 调用目标字符串
	 */
	private String invokeTarget;

	/**
	 * cron执行表达式
	 */
	private String cronExpression;

	/**
	 * 计划执行错误策略(1:立即执行 2:执行一次 3:放弃执行)
	 */
	private String misfirePolicy;

	/**
	 * 是否并发执行(0:允许 1:禁止)
	 */
	private String concurrent;

	/**
	 * 状态(0:正常 1:暂停)
	 */
	private String status;

	/**
	 * 备注
	 */
	private String remark;
}
