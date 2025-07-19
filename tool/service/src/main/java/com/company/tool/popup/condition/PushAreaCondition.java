package com.company.tool.popup.condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 推送地区
 */
@Slf4j
@Component("PushAreaCondition")
public class PushAreaCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		String popConditionValue = popParam.getPopConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> cityCodeMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String cityCodes = cityCodeMap.get("cityCodes");
		// 全国
		if ("0".equals(cityCodes)) {
			return true;
		}

		List<String> cityCodeList = Arrays.asList(StringUtils.split(cityCodes, ","));

		Map<String, String> runtimeAttach = popParam.getRuntimeAttach();
		String cityCode = runtimeAttach.get("cityCode");

		boolean canPop = cityCodeList.contains(cityCode);
		if (!canPop) {
			log.info("{}条件不满足,配置城市:{},当前城市:{}", popParam.getPopupId(), cityCodes, cityCode);
			return false;
		}
		return canPop;
	}
}
