package com.company.app.rabbitmq.consumer.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.XDELAYMESSAGE_STRATEGY)
public class XDelayMessageStrategy implements BaseStrategy<Map<String, Object>> {

	@Override
	public void doStrategy(Map<String, Object> params) {
		log.info("xDelayMessageStrategy:{}", params);
	}
}
