package com.company.tool.nav.condition;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.tool.nav.NavShowCondition;
import com.company.tool.nav.ShowParam;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 指定时间段
 * 08:00:00~10:00:00 表示当天8点~10点可显示
 * 10:00:00~08:00:00 表示当天10点~第2天8点可显示
 * </pre>
 */
@Slf4j
@Component(NavShowCondition.PREFIX + "TimeRangeCondition")
public class TimeRangeCondition implements NavShowCondition {

	@Override
	public boolean canShow(ShowParam showParam) {
		String showConditionValue = showParam.getShowConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> showConditionValueMap = JsonUtil.toEntity(showConditionValue, Map.class);
		String beginTimeStr = showConditionValueMap.get("beginTime");
		String endTimeStr = showConditionValueMap.get("endTime");

		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime beginTime = LocalTime.parse(beginTimeStr, pattern);
		LocalTime endTime = LocalTime.parse(endTimeStr, pattern);

		LocalTime beginOfTime = LocalTime.of(0, 0);
		long seconds = beginOfTime.until(beginTime, ChronoUnit.SECONDS);

		beginTime = beginTime.minus(seconds, ChronoUnit.SECONDS);
		endTime = endTime.minus(seconds, ChronoUnit.SECONDS);

		LocalTime now = LocalTime.now();
		now = now.minus(seconds, ChronoUnit.SECONDS);

		boolean canShow = now.isAfter(beginTime) && now.isBefore(endTime);
		if (!canShow) {
			String beginTimeFormat = beginTime.format(pattern);
			String endTimeFormat = endTime.format(pattern);
			log.info("{}条件不满足,当前时间{}不在指定时间段{}-{}内", showParam.getNavItemId(), now.format(pattern), beginTimeFormat,
					endTimeFormat);
			return false;
		}
		return true;
	}
}
