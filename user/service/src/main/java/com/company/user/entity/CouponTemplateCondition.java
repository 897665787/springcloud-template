package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("coupon_template_condition")
public class CouponTemplateCondition {
	private Integer id;

	/**
	 * coupon_template.id
	 */
	private Integer couponTemplateId;

	/**
	 * 使用条件(bean名称,coupon_use_condition.bean_name)
	 */
	private String useCondition;
	/**
	 * 使用条件值
	 */
	private String useConditionValue;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
