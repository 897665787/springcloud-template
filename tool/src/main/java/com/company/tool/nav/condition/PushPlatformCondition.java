package com.company.tool.nav.condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.nav.NavShowCondition;
import com.company.tool.nav.ShowParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 推送平台
 */
@Slf4j
@Component(NavShowCondition.PREFIX + "PushPlatformCondition")
public class PushPlatformCondition implements NavShowCondition {

	@Override
	public boolean canShow(ShowParam showParam) {
		Map<String, String> runtimeAttach = showParam.getRuntimeAttach();
		String platform = runtimeAttach.get("x-platform");

		String showConditionValue = showParam.getShowConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> platformMap = JsonUtil.toEntity(showConditionValue, Map.class);
		String platforms = platformMap.get("platforms");
		List<String> platformList = Arrays.asList(StringUtils.split(platforms, ","));

		boolean canShow = platformList.contains(platform);
		if (!canShow) {
			log.info("{}条件不满足,配置平台:{},实际平台:{}", showParam.getNavItemId(), platforms, platform);
			return false;
		}
		return true;
	}
}