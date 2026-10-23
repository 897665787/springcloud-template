package com.company.system.entity;

/**
 * 通知公告
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.datasource.mybatisplus.activerecord.AuditableModel;

import lombok.Data;

/**
 * 通知公告
 */
@Data
@TableName("sys_notice")
public class SysNotice extends AuditableModel<SysNotice> {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 公告标题
	 */
	private String noticeTitle;

	/**
	 * 公告类型(1:通知,2:公告)
	 */
	private String noticeType;

	/**
	 * 公告内容
	 */
	private String noticeContent;

	/**
	 * 公告状态(0:正常,1:关闭)
	 */
	private String status;

	/**
	 * 备注
	 */
	private String remark;
}
