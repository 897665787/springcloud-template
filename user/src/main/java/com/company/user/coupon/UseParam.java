package com.company.user.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
	 * 优惠券信息
	 */
	UserCouponInfo userCouponInfo;
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
	
	@Data
	@Builder
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class UserCouponInfo {
		/**
		 * 券模板ID
		 */
		Integer couponTemplateId;
		/**
		 * 用户ID
		 */
		Integer userId;
		/**
		 * 状态(nouse:未使用/已使用/已过期/未激活/已失效)
		 */
		String status;
		/**
		 * 满x金额可用
		 */
		BigDecimal conditionAmount;

		/**
		 * 有效期开始时间
		 */
		LocalDateTime beginTime;

		/**
		 * 有效期结束时间
		 */
		LocalDateTime endTime;
	}
}