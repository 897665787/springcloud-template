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
 * 指定用户
 */
@Slf4j
@Component("SpecifyUserCondition")
public class SpecifyUserCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Integer userId = popParam.getUserId();

		if (userId == null || userId <= 0) {
			log.info("{}条件不满足,userId:{}", popParam.getPopupId(), userId);
			return false;
		}
		
		String popConditionValue = popParam.getPopConditionValue();
		
		@SuppressWarnings("unchecked")
		Map<String, String> popConditionValueMap = JsonUtil.toEntity(popConditionValue, Map.class);
		String userIds = popConditionValueMap.get("userIds");
		
		List<String> userIdList = Arrays.asList(StringUtils.split(userIds, ","));

		boolean canPop = userIdList.contains(userId.toString());
		if (!canPop) {
			log.info("{}条件不满足,配置用户ID:{},当前用户ID:{}", popParam.getPopupId(), userIds, userId);
			return false;
		}
		return canPop;
	}
}