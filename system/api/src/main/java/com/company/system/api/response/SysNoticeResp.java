package com.company.system.api.response;

/**
 * 通知公告响应
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 通知公告响应
 */
@Data
public class SysNoticeResp {

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

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}
