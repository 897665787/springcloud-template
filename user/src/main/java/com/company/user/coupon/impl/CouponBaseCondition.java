package com.company.user.coupon.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.company.user.coupon.UseCondition;
import com.company.user.coupon.UseParam;
import com.company.user.coupon.UseParam.UserCouponInfo;
import com.company.user.coupon.dto.MatchResult;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 优惠券基础条件（必选）
 */
@Slf4j
@Component("CouponBaseCondition")
public class CouponBaseCondition implements UseCondition {

	@Override
	public MatchResult canUse(UseParam useParam) {
		UserCouponInfo userCoupon = useParam.getUserCouponInfo();

		// 校验用户
		Integer appUserId = userCoupon.getUserId();
		boolean canUse = appUserId.equals(useParam.getUserId());
		if (!canUse) {
			log.info("{}校验用户条件不成立:{} {}", useParam.getUserCouponId(), appUserId, useParam.getUserId());
			return MatchResult.builder().canUse(false).reason("校验用户条件不成立").build();
		}
		
		// 未使用
		String status = userCoupon.getStatus();
		canUse = "nouse".equals(status);
		if (!canUse) {
			log.info("{}条件不满足,未使用:{}", useParam.getUserCouponId(), status);
			return MatchResult.builder().canUse(false).reason("优惠券状态不为未使用").build();
		}

		// 有效期
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime beginTime = userCoupon.getBeginTime();
		LocalDateTime endTime = userCoupon.getEndTime();
		canUse = now.isAfter(beginTime) && now.isBefore(endTime);
		if (!canUse) {
			log.info("{}条件不满足,有效期:{}/{}/{}", useParam.getUserCouponId(), LocalDateTimeUtil.formatNormal(beginTime),
					LocalDateTimeUtil.formatNormal(now), LocalDateTimeUtil.formatNormal(endTime));
			return MatchResult.builder().canUse(false).reason("优惠券不在有效期内").build();
		}

		// 满X金额可用
		BigDecimal conditionAmount = userCoupon.getConditionAmount();
		canUse = useParam.getOrderAmount().compareTo(conditionAmount) >= 0;
		if (!canUse) {
			log.info("{}条件不满足,金额满{}可用,订单金额:{}", useParam.getUserCouponId(), conditionAmount, useParam.getOrderAmount());
			String reason = String.format("订单金额需满%s元", conditionAmount.toPlainString());
			return MatchResult.builder().canUse(false).reason(reason).build();
		}
		
		return MatchResult.builder().canUse(true).build();
	}
}