package com.company.tool.popup.condition;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.tool.popup.PopCondition;
import com.company.tool.popup.PopParam;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * 未下单用户
 */
@Slf4j
@Component("UnOrderUserCondition")
public class UnOrderUserCondition implements PopCondition {

	@Override
	public boolean canPop(PopParam popParam) {
		Integer userId = popParam.getUserId();
		if (userId == null) {
			log.info("{}条件不满足,未注册");
			return false;
		}

		// 查询用户的订单
		List<String> orderList = Lists.newArrayList();

		boolean canPop = orderList.size() == 0;
		if (!canPop) {
			log.info("{}条件不满足,用户ID:{},订单数:{}", popParam.getPopupId(), userId, orderList.size());
			return false;
		}
		return true;
	}
}