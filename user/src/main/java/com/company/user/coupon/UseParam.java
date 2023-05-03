package com.company.user.coupon;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UseParam {
	/* 必填参数 */
	/**
	 * 用户优惠券ID
	 */
	Integer userCouponId;
	/**
	 * 用户ID
	 */
	Integer userId;
	/**
	 * 订单金额
	 */
	BigDecimal orderAmount;

	/* 运行时参数 */
	/**
	 * 运行时附带参数（一般是调用处和UseCondition实现类约定好）
	 */
	Map<String, String> runtimeAttach;

	/* 配置参数 */
	/**
	 * 使用条件值
	 */
	String useConditionValue;
}