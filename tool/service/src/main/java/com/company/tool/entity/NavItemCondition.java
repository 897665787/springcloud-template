package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("nav_item_condition")
public class NavItemCondition {
	private Integer id;

	/**
	 * navItem.id
	 */
	private Integer navItemId;

	/**
	 * 展示条件(bean名称,nav_item_show_condition.bean_name)
	 */
	private String showCondition;
	/**
	 * 展示条件值
	 */
	private String showConditionValue;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
