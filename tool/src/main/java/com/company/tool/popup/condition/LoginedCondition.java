package com.company.tool.popup.condition;

import org.springframework.stereotype.Component;

import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 已登录用户
 */
@Slf4j
@Component("LoginedCondition")
public class LoginedCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Integer userId = popParam.getUserId();

		if (userId == null || userId <= 0) {
			log.info("{}用户未登录,userId:{}", popParam.getPopupId(), userId);
			return false;
		}
		return true;
	}
}