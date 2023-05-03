package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("mk_coupon_use_condition")
public class CouponUseCondition {
	private Integer id;

	/**
	 * bean名称(UseCondition的实现类)
	 */
	private String beanName;

	/**
	 * 描述
	 */
	private String descrpition;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
