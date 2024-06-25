package com.company.tool.amqp.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.BaseStrategy;
import com.company.tool.amqp.strategy.dto.SendEmailMQDto;
import com.company.tool.email.EmailSenderConsumer;

@Component(StrategyConstants.SENDEMAIL_STRATEGY)
public class SendEmailStrategy implements BaseStrategy<SendEmailMQDto> {

	@Autowired
	private EmailSenderConsumer emailSenderConsumer;

	@Override
	public void doStrategy(SendEmailMQDto paremail) {
		Integer smsTaskDetailId = paremail.getEmailTaskDetailId();
		emailSenderConsumer.consumer(smsTaskDetailId);
	}
}
