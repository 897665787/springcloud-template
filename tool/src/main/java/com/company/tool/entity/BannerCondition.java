package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("mk_banner_condition")
public class BannerCondition {
	private Integer id;

	/**
	 * mk_banner.id
	 */
	private Integer bannerId;

	/**
	 * 展示条件(bean名称,mk_banner_show_condition.bean_name)
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
