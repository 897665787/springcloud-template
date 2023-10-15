package com.company.tool.amqp.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.BaseStrategy;
import com.company.tool.amqp.strategy.dto.SendSubscribeMQDto;
import com.company.tool.subscribe.SubscribeSenderConsumer;

@Component(StrategyConstants.SENDSUBSCRIBE_STRATEGY)
public class SendSubscribeStrategy implements BaseStrategy<SendSubscribeMQDto> {

	@Autowired
	private SubscribeSenderConsumer subscribeSenderConsumer;

	@Override
	public void doStrategy(SendSubscribeMQDto params) {
		Integer subscribeTaskDetailId = params.getSubscribeTaskDetailId();
		subscribeSenderConsumer.consumer(subscribeTaskDetailId);
	}
}
