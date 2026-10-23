package com.company.adminapi.excel;

/**
 * 定时任务导出
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
 * 定时任务导出
 */
@Data
public class SysJobExcel {

	/**
	 * 任务id
	 */
	@ExcelProperty(value = "任务id")
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
	 * cron执行表达式
	 */
	@ExcelProperty(value = "cron执行表达式")
	private String cronExpression;

	/**
	 * 状态(0:正常,1:暂停)
	 */
	@ExcelProperty(value = "状态(0:正常,1:暂停)")
	private String status;

	/**
	 * 备注
	 */
	@ExcelProperty(value = "备注")
	private String remark;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ExcelProperty(value = "更新时间")
	private LocalDateTime updateTime;
}
