package com.company.user.coupon.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCouponMe {
	Integer userCouponId;// 用户优惠券ID
	
	/**
	 * 优惠券名称
	 */
	private String name;

	/**
	 * 最大优惠金额
	 */
	private BigDecimal maxAmount;
	
	/**
	 * 折扣
	 */
	private BigDecimal discount;

	/**
	 * 满x金额可用
	 */
	private BigDecimal conditionAmount;
	
	/**
	 * 有效期结束时间
	 */
	private LocalDateTime endTime;
}