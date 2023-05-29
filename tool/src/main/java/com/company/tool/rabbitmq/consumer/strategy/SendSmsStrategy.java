package com.company.tool.rabbitmq.consumer.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.BaseStrategy;
import com.company.tool.rabbitmq.dto.SendSmsMQDto;
import com.company.tool.sms.SmsSenderConsumer;

@Component(StrategyConstants.SENDSMS_STRATEGY)
public class SendSmsStrategy implements BaseStrategy<SendSmsMQDto> {

	@Autowired
	private SmsSenderConsumer amsSenderConsumer;

	@Override
	public void doStrategy(SendSmsMQDto params) {
		Integer smsTaskId = params.getSmsTaskId();
		amsSenderConsumer.consumer(smsTaskId);
	}
}
