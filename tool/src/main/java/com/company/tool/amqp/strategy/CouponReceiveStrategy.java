package com.company.tool.amqp.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.framework.amqp.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.COUPON_RECEIVE_STRATEGY)
public class CouponReceiveStrategy implements BaseStrategy<Map<String, Object>> {

	@Override
	public void doStrategy(Map<String, Object> params) {
		log.info("params:{}", params);
	}
}
