package com.company.tool.nav.condition;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.tool.nav.NavShowCondition;
import com.company.tool.nav.ShowParam;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 指定星期几
 */
@Slf4j
@Component(NavShowCondition.PREFIX + "WeekDayCondition")
public class WeekDayCondition implements NavShowCondition {

	@Override
	public boolean canShow(ShowParam showParam) {
		// 获得指定日期是星期几，1表示周日，2表示周一
		int dayOfWeek = DateUtil.dayOfWeek(new Date());

		String showConditionValue = showParam.getShowConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> showConditionValueMap = JsonUtil.toEntity(showConditionValue, Map.class);
		String weekDays = showConditionValueMap.get("weekDays");

		List<String> weekDayList = Arrays.stream(weekDays.split(",")).collect(Collectors.toList());

		boolean canShow = weekDayList.contains(String.valueOf(dayOfWeek));
		if (!canShow) {
			log.info("{}条件不满足,配置星期:{},当前星期:{}", showParam.getNavItemId(), weekDays, dayOfWeek);
			return false;
		}
		return true;
	}
}
