package com.company.tool.messagedriven.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.BaseStrategy;
import com.company.tool.messagedriven.strategy.dto.SendSmsMQDto;
import com.company.tool.sms.SmsSenderConsumer;

@Component(StrategyConstants.SENDSMS_STRATEGY)
public class SendSmsStrategy implements BaseStrategy<SendSmsMQDto> {

	@Autowired
	private SmsSenderConsumer amsSenderConsumer;

	@Override
	public void doStrategy(SendSmsMQDto params) {
		Integer smsTaskDetailId = params.getSmsTaskDetailId();
		amsSenderConsumer.consumer(smsTaskDetailId);
	}
}
