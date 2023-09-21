package com.company.tool.popup.condition;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 注册时间n小时内用户
 */
@Slf4j
@Component("RegisterNHoursUserCondition")
public class RegisterNHoursUserCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Integer userId = popParam.getUserId();
		if (userId == null) {
			log.info("{}条件不满足,未注册");
			return false;
		}

		LocalDateTime registerTime = LocalDateTime.of(2023, 8, 29, 0, 0);

		String popConditionValue = popParam.getPopConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> popConditionMap = JsonUtil.toEntity(popConditionValue, Map.class);
		Integer hours = MapUtils.getInteger(popConditionMap, "hours");

		LocalDateTime now = LocalDateTime.now();
		long betweenHours = LocalDateTimeUtil.between(registerTime, now, ChronoUnit.HOURS);

		boolean canPop = betweenHours < hours;
		if (!canPop) {
			log.info("{}条件不满足,注册时间:{},配置小时:{}", popParam.getPopupId(), registerTime, hours);
			return false;
		}
		return true;
	}
}