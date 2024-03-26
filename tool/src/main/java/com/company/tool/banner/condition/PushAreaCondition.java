package com.company.tool.banner.condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.banner.BannerShowCondition;
import com.company.tool.banner.ShowParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 推送地区
 */
@Slf4j
@Component(BannerShowCondition.PREFIX + "PushAreaCondition")
public class PushAreaCondition implements BannerShowCondition {

	@Override
	public boolean canShow(ShowParam showParam) {
		String showConditionValue = showParam.getShowConditionValue();

		@SuppressWarnings("unchecked")
		Map<String, String> cityCodeMap = JsonUtil.toEntity(showConditionValue, Map.class);
		String cityCodes = cityCodeMap.get("cityCodes");
		// 全国
		if ("0".equals(cityCodes)) {
			return true;
		}

		List<String> cityCodeList = Arrays.asList(StringUtils.split(cityCodes, ","));

		Map<String, String> runtimeAttach = showParam.getRuntimeAttach();
		String cityCode = runtimeAttach.get("cityCode");

		boolean canShow = cityCodeList.contains(cityCode);
		if (!canShow) {
			log.info("{}条件不满足,配置城市:{},当前城市:{}", showParam.getBannerId(), cityCodes, cityCode);
			return false;
		}
		return canShow;
	}
}