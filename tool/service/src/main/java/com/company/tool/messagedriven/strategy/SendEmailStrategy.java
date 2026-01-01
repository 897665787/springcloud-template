package com.company.tool.messagedriven.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.BaseStrategy;
import com.company.tool.messagedriven.strategy.dto.SendEmailMQDto;
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
