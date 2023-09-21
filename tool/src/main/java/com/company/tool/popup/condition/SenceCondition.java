package com.company.tool.popup.condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 场景
 */
@Slf4j
@Component("SenceCondition")
public class SenceCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Map<String, String> runtimeAttach = popParam.getRuntimeAttach();
		String sence = runtimeAttach.get("sence");

		String popConditionValue = popParam.getPopConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> popConditionMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String sences = popConditionMap.get("sences");
		List<String> senceList = Arrays.asList(StringUtils.split(sences, ","));

		boolean canPop = senceList.contains(sence);
		if (!canPop) {
			log.info("{}条件不满足,配置场景:{},实际场景:{}", popParam.getPopupId(), sences, sence);
			return false;
		}
		return true;
	}
}