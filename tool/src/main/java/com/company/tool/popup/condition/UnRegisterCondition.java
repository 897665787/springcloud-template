package com.company.tool.popup.condition;

import org.springframework.stereotype.Component;

import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 未注册用户
 */
@Slf4j
@Component("UnRegisterCondition")
public class UnRegisterCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Integer userId = popParam.getUserId();
		if (userId != null) {
			log.info("{}条件不满足,userId:{}", userId);
			return false;
		}
		
		// 根据openid或者设备号查询是否注册过
		
		return true;
	}
}