package com.company.tool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("app_version")
public class AppVersion {
	private Integer id;

	/**
	 * app_info.app_code
	 */
	private String appCode;

	/**
	 * 版本号
	 */
	private String version;

	/**
	 * 最低支持版本（低于此版本必须升级）
	 */
	private String minSupportedVersion;

	/**
	 * 发布时间
	 */
	private LocalDateTime releaseTime;

	/**
	 * 安装包下载地址
	 */
	private String downloadUrl;

	/**
	 * 发布说明
	 */
	private String releaseNotes;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private String createBy;
	private String updateBy;
}
