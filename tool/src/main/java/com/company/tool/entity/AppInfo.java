package com.company.tool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("app_info")
public class AppInfo {
	private Integer id;

	/**
	 * 唯一标识（写死到客户端）
	 */
	private String appCode;

	/**
	 * 名称
	 */
	private String appName;

	/**
	 * 平台(Android、iOS)
	 */
	private String platform;

	/**
	 * 图标
	 */
	private String logo;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private String createBy;
	private String updateBy;
}
