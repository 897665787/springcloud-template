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
 * 推送平台
 */
@Slf4j
@Component("PushPlatformCondition")
public class PushPlatformCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Map<String, String> runtimeAttach = popParam.getRuntimeAttach();
		String platform = runtimeAttach.get("x-platform");

		String popConditionValue = popParam.getPopConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> platformMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String platforms = platformMap.get("platforms");
		List<String> platformList = Arrays.asList(StringUtils.split(platforms, ","));

		boolean canPop = platformList.contains(platform);
		if (!canPop) {
			log.info("{}条件不满足,配置平台:{},实际平台:{}", popParam.getPopupId(), platforms, platform);
			return false;
		}
		return true;
	}
}
