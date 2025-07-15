package com.company.user.coupon.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.user.coupon.UseCondition;
import com.company.user.coupon.UseParam;
import com.company.user.coupon.dto.MatchResult;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * 指定时间段可用
 */
@Slf4j
@Component("TimeRangeCondition")
public class TimeRangeCondition implements UseCondition {

	@Override
	public MatchResult canUse(UseParam useParam) {
		String useConditionValue = useParam.getUseConditionValue();

		JsonNode useConditionJson = JsonUtil.toJsonNode(useConditionValue);
		String beginTimeStr = JsonUtil.getString(useConditionJson, "beginTime");
		String endTimeStr = JsonUtil.getString(useConditionJson, "endTime");

		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime beginTime = LocalTime.parse(beginTimeStr, pattern);
		LocalTime endTime = LocalTime.parse(endTimeStr, pattern);

		LocalTime now = LocalTime.now();
		boolean canUse = now.isAfter(beginTime) && now.isBefore(endTime);
		if (!canUse) {
			String beginTimeFormat = beginTime.format(pattern);
			String endTimeFormat = endTime.format(pattern);
			log.info("{}条件不满足,当前时间{}不在指定时间段{}-{}内", useParam.getUserCouponId(), now.format(pattern), beginTimeFormat,
					endTimeFormat);
			String reason = String.format("仅时间段%s至%s可用", beginTimeFormat, beginTimeFormat);
			return MatchResult.builder().canUse(false).reason(reason).build();
		}
		return MatchResult.builder().canUse(true).build();
	}
}
