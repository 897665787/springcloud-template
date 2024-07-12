package com.company.tool.amqp.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.BaseStrategy;
import com.company.tool.amqp.strategy.dto.SendWebhookMQDto;
import com.company.tool.webhook.WebhookSenderConsumer;

@Component(StrategyConstants.SENDWEBHOOK_STRATEGY)
public class SendWebhookStrategy implements BaseStrategy<SendWebhookMQDto> {

	@Autowired
	private WebhookSenderConsumer webhookSenderConsumer;

	@Override
	public void doStrategy(SendWebhookMQDto parwebhook) {
		Integer smsTaskId = parwebhook.getWebhookTaskId();
		webhookSenderConsumer.consumer(smsTaskId);
	}
}
