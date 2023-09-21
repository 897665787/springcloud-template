package com.company.tool.popup.condition;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 指定时间段
 * 08:00:00~10:00:00 表示当天8点~10点可弹窗
 * 10:00:00~08:00:00 表示当天10点~第2天8点可弹窗
 * </pre>
 */
@Slf4j
@Component("PopTimeRangeCondition")
public class PopTimeRangeCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		String popConditionValue = popParam.getPopConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> popConditionValueMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String beginTimeStr = popConditionValueMap.get("beginTime");
		String endTimeStr = popConditionValueMap.get("endTime");

		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime beginTime = LocalTime.parse(beginTimeStr, pattern);
		LocalTime endTime = LocalTime.parse(endTimeStr, pattern);

		LocalTime beginOfTime = LocalTime.of(0, 0);
		long seconds = beginOfTime.until(beginTime, ChronoUnit.SECONDS);
		
		beginTime = beginTime.minus(seconds, ChronoUnit.SECONDS);
		endTime = endTime.minus(seconds, ChronoUnit.SECONDS);
		
		LocalTime now = LocalTime.now();
		now = now.minus(seconds, ChronoUnit.SECONDS);
		
		boolean canPop = now.isAfter(beginTime) && now.isBefore(endTime);
		if (!canPop) {
			String beginTimeFormat = beginTime.format(pattern);
			String endTimeFormat = endTime.format(pattern);
			log.info("{}条件不满足,当前时间{}不在指定时间段{}-{}内", popParam.getPopupId(), now.format(pattern), beginTimeFormat,
					endTimeFormat);
			return false;
		}
		return true;
	}
}