package com.company.user.coupon;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeeParam {
	/* 必填参数 */
	/**
	 * 用户优惠券ID
	 */
	Integer userCouponId;
	/**
	 * 用户ID
	 */
	Integer userId;

	/* 运行时参数 */
	/**
	 * 运行时附带参数（可以普通字符串或json字符串，一般是调用处和UseCondition实现类约定好）
	 */
	Map<String, String> runtimeAttach;

	/* 配置参数 */
	/**
	 * 使用条件值
	 */
	String useConditionValue;
}