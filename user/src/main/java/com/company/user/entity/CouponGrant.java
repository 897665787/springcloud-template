package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("mk_coupon_grant")
public class CouponGrant {
	private Integer id;

	/**
	 * mk_coupon_template.id
	 */
	private Integer couponTemplateId;

	/**
	 * mk_coupon_template.name
	 */
	private String name;

	/**
	 * 发放量
	 */
	private Integer totalNum;

	/**
	 * 实际发放量
	 */
	private Integer actualNum;

	/**
	 * 发放时间
	 */
	private LocalDateTime grantTime;

	/**
	 * 发放条件
	 */
	private String grantCondition;

	/**
	 * 领取条件
	 */
	private String drawCondition;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
