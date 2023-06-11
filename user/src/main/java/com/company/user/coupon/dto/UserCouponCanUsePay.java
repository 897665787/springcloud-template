package com.company.user.coupon.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCouponCanUsePay {
	Integer userCouponId;// 用户优惠券ID

	/**
	 * 优惠券名称
	 */
	String name;

	/**
	 * 最大优惠金额
	 */
	BigDecimal maxAmount;
	
	/**
	 * 折扣
	 */
	BigDecimal discount;

	/**
	 * 满x金额可用
	 */
	BigDecimal conditionAmount;
	
	/**
	 * 有效期结束时间
	 */
	LocalDateTime endTime;
	
	Boolean canUse;// 能用
	BigDecimal reduceAmount;// 优惠金额
	String reason;// 不可用原因(canUse=false有值)
}