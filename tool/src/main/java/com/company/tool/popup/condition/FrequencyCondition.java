package com.company.tool.popup.condition;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;
import com.company.tool.service.market.PopupLogService;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 弹窗频率
 */
@Slf4j
@Component("FrequencyCondition")
public class FrequencyCondition implements PopCondition {

	@Autowired
	private PopupLogService popupLogService;

	@Override
	public boolean canPop(PopParam popParam) {
		Integer popupId = popParam.getPopupId();
		Integer userId = popParam.getUserId();
		String deviceid = popParam.getDeviceid();
		LocalDateTime lastPopupTime = popupLogService.lastPopupTimeByBusiness(PopupEnum.LogBusinessType.POPUP, popupId,
				userId, deviceid);
		if (lastPopupTime == null) {// 没弹过，肯定要弹1次
			return true;
		}

		String popConditionValue = popParam.getPopConditionValue();
		@SuppressWarnings("unchecked")
		Map<String, String> popConditionMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String frequency = popConditionMap.get("frequency");

		if ("once".equals(frequency)) {// 只弹1次
			if (lastPopupTime != null) {
				log.info("{}条件不满足,只弹1次,上次弹窗时间:{}}", popParam.getPopupId(), lastPopupTime);
				return false;
			}
		} else if ("times".equals(frequency)) {// n天/次
			Integer days = MapUtils.getInteger(popConditionMap, "count");

			LocalDateTime beginTime = LocalDateTimeUtil.beginOfDay(lastPopupTime);

			LocalDateTime now = LocalDateTime.now();
			long diffDays = LocalDateTimeUtil.between(beginTime, now, ChronoUnit.DAYS);
			if (diffDays < days) {
				log.info("{}条件不满足,{}天/次,当前天数:{}", popParam.getPopupId(), days, diffDays);
				return false;
			}
		} else if ("daily".equals(frequency)) {// n次/天
			Integer times = MapUtils.getInteger(popConditionMap, "count");
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime beginTime = LocalDateTimeUtil.beginOfDay(now);
			LocalDateTime endTime = LocalDateTimeUtil.endOfDay(now);
			Integer popupCount = popupLogService.countByBusiness(PopupEnum.LogBusinessType.POPUP, popupId, userId,
					deviceid, beginTime, endTime);
			if (popupCount >= times) {
				log.info("{}条件不满足,{}次/天,当前次数:{}", popParam.getPopupId(), times, popupCount);
				return false;
			}
		}
		return true;
	}
}