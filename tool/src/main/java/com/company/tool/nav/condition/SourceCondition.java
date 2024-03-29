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
 * 来源source
 */
@Slf4j
@Component(NavShowCondition.PREFIX + "SourceCondition")
public class SourceCondition implements NavShowCondition {

	@Override
	public boolean canShow(ShowParam showParam) {
		Map<String, String> runtimeAttach = showParam.getRuntimeAttach();
		String source = runtimeAttach.get("source");
		if (StringUtils.isBlank(source)) {
			log.info("{}条件不满足,当前来源为空", showParam.getNavItemId());
			return false;
		}

		String showConditionValue = showParam.getShowConditionValue();
		@SuppressWarnings("unchecked")
		Map<String, String> sourceMap = JsonUtil.toEntity(showConditionValue, Map.class);
		String sources = sourceMap.get("sources");
		List<String> sourceList = Arrays.asList(StringUtils.split(sources, ","));

		boolean canShow = sourceList.contains(source);
		if (!canShow) {
			log.info("{}条件不满足,配置来源:{},当前来源:{}", showParam.getNavItemId(), sources, source);
			return false;
		}
		return canShow;
	}
}