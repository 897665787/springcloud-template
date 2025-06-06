package com.company.tool.messagedriven.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.COUPON_EXPIRE_STRATEGY)
public class CouponExpireStrategy implements BaseStrategy<Map<String, Object>> {

	@Override
	public void doStrategy(Map<String, Object> params) {
		log.info("params:{}", params);
	}
}
