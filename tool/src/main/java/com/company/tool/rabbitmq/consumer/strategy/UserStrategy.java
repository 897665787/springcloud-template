package com.company.tool.rabbitmq.consumer.strategy;

import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.BaseStrategy;
import com.company.tool.rabbitmq.dto.UserMQDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.USER_STRATEGY)
public class UserStrategy implements BaseStrategy<UserMQDto> {

	@Override
	public void doStrategy(UserMQDto params) {
		log.info("commonStrategy:{}", params);
	}
}
