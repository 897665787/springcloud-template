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
 * 来源source
 */
@Slf4j
@Component("SourceCondition")
public class SourceCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Map<String, String> runtimeAttach = popParam.getRuntimeAttach();
		String source = runtimeAttach.get("source");
		if (StringUtils.isBlank(source)) {
			log.info("{}条件不满足,当前来源为空", popParam.getPopupId());
			return false;
		}

		String popConditionValue = popParam.getPopConditionValue();
		@SuppressWarnings("unchecked")
		Map<String, String> sourceMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String sources = sourceMap.get("sources");
		List<String> sourceList = Arrays.asList(StringUtils.split(sources, ","));

		boolean canPop = sourceList.contains(source);
		if (!canPop) {
			log.info("{}条件不满足,配置来源:{},当前来源:{}", popParam.getPopupId(), sources, source);
			return false;
		}
		return canPop;
	}
}