package com.company.system.entity;

/**
 * 定时任务日志
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.datasource.mybatisplus.activerecord.AuditableModel;

import lombok.Data;

/**
 * 定时任务日志
 */
@Data
@TableName("sys_job_log")
public class SysJobLog extends AuditableModel<SysJobLog> {

	/**
	 * 日志id
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
	 * 日志信息
	 */
	private String jobMessage;

	/**
	 * 执行状态(0:正常 1:失败)
	 */
	private String status;

	/**
	 * 异常信息
	 */
	private String exceptionInfo;

	/**
	 * 执行开始时间
	 */
	private LocalDateTime startTime;

	/**
	 * 执行结束时间
	 */
	private LocalDateTime endTime;
}
