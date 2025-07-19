package com.company.tool.nav.condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.tool.nav.NavShowCondition;
import com.company.tool.nav.ShowParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 场景
 */
@Slf4j
@Component(NavShowCondition.PREFIX + "SenceCondition")
public class SenceCondition implements NavShowCondition {

	@Override
	public boolean canShow(ShowParam showParam) {
		Map<String, String> runtimeAttach = showParam.getRuntimeAttach();
		String sence = runtimeAttach.get("sence");

		String showConditionValue = showParam.getShowConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> showConditionMap = JsonUtil.toEntity(showConditionValue, Map.class);
		String sences = showConditionMap.get("sences");
		List<String> senceList = Arrays.asList(StringUtils.split(sences, ","));

		boolean canShow = senceList.contains(sence);
		if (!canShow) {
			log.info("{}条件不满足,配置场景:{},实际场景:{}", showParam.getNavItemId(), sences, sence);
			return false;
		}
		return true;
	}
}
