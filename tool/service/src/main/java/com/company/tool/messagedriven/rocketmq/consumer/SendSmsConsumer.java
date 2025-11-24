package com.company.tool.messagedriven.rocketmq.consumer;

import com.company.framework.messagedriven.rocketmq.RocketMQAutoConfiguration;
import com.company.framework.messagedriven.rocketmq.utils.ConsumerUtils;
import com.company.tool.messagedriven.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;

@Component
@RocketMQMessageListener(
		topic = Constants.EXCHANGE.DIRECT,
		consumerGroup = Constants.QUEUE.SEND_SMS.NAME,
		selectorType = SelectorType.TAG,
		selectorExpression = Constants.QUEUE.SEND_SMS.KEY
)
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class SendSmsConsumer implements RocketMQListener<MessageExt> {
	@Override
	public void onMessage(MessageExt messageExt) {
		Map<String, String> properties = messageExt.getProperties();
		String jsonStrMsg = new String(messageExt.getBody(), Charset.forName("UTF-8"));
		ConsumerUtils.handleByStrategy(jsonStrMsg, properties);
	}
}
