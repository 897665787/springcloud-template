package com.company.tool.popup.condition;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 指定星期几
 */
@Slf4j
@Component("WeekDayCondition")
public class WeekDayCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		// 获得指定日期是星期几，1表示周日，2表示周一
		int dayOfWeek = DateUtil.dayOfWeek(new Date());

		String popConditionValue = popParam.getPopConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> popConditionValueMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String weekDays = popConditionValueMap.get("weekDays");

		List<String> weekDayList = Arrays.stream(weekDays.split(",")).collect(Collectors.toList());

		boolean canPop = weekDayList.contains(String.valueOf(dayOfWeek));
		if (!canPop) {
			log.info("{}条件不满足,配置星期:{},当前星期:{}", popParam.getPopupId(), weekDays, dayOfWeek);
			return false;
		}
		return true;
	}
}
