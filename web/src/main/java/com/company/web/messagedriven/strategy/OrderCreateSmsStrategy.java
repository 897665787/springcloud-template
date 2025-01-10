package com.company.web.messagedriven.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.messagedriven.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.ORDERCREATE_SMS_STRATEGY)
public class OrderCreateSmsStrategy implements BaseStrategy<Map<String, Object>> {

	@Override
	public void doStrategy(Map<String, Object> params) {
		log.info("sms params:{}", JsonUtil.toJsonString(params));
	}
}
