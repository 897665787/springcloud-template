package com.company.adminapi.excel;

/**
 * 定时任务日志导出
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * 定时任务日志导出
 */
@Data
public class SysJobLogExcel {

	/**
	 * 日志id
	 */
	@ExcelProperty(value = "日志id")
	private Integer id;

	/**
	 * 任务名称
	 */
	@ExcelProperty(value = "任务名称")
	private String jobName;

	/**
	 * 任务组名
	 */
	@ExcelProperty(value = "任务组名")
	private String jobGroup;

	/**
	 * 调用目标字符串
	 */
	@ExcelProperty(value = "调用目标字符串")
	private String invokeTarget;

	/**
	 * 日志信息
	 */
	@ExcelProperty(value = "日志信息")
	private String jobMessage;

	/**
	 * 执行状态(0:正常,1:失败)
	 */
	@ExcelProperty(value = "执行状态(0:正常,1:失败)")
	private String status;

	/**
	 * 异常信息
	 */
	@ExcelProperty(value = "异常信息")
	private String exceptionInfo;

	/**
	 * 执行开始时间
	 */
	@ExcelProperty(value = "执行开始时间")
	private LocalDateTime startTime;

	/**
	 * 执行结束时间
	 */
	@ExcelProperty(value = "执行结束时间")
	private LocalDateTime endTime;
}
