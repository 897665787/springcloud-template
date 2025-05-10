package com.company.tool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@TableName("push_device_bind")
@Data
@Accessors(chain = true)
public class PushDeviceBind {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 设备ID
	 */
	private String deviceid;
	/**
	 * 推送ID（推送平台唯一）
	 */
	private String pushId;
	/**
	 * 设备类型(Android:安卓,iOS:苹果)
	 */
	private String deviceType;

	/**
	 * 备注信息
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