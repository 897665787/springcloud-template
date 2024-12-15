package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("subscribe_group_type")
@Data
@Accessors(chain = true)
public class SubscribeGroupType {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 订阅组
	 */
	private String group;
	/**
	 * 业务类型(多个英文逗号分隔,最多3个)
	 */
	private String types;
	
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