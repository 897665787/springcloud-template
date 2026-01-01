package com.company.tool.messagedriven.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.BaseStrategy;
import com.company.tool.messagedriven.strategy.dto.SendWebhookMQDto;
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
