package com.company.order.messagedriven.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.messagedriven.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.SMS_STRATEGY)
public class SmsStrategy implements BaseStrategy<Map<String, Object>> {

	@Override
	public void doStrategy(Map<String, Object> params) {
		log.info("sms params:{}", JsonUtil.toJsonString(params));
	}
}