package com.company.order.messagedriven.rocketmq.consumer;

import com.company.framework.messagedriven.rocketmq.RocketMQAutoConfiguration;
import com.company.order.messagedriven.Constants;
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
		topic = "${messagedriven.exchange.direct}",
		consumerGroup = Constants.QUEUE.PAY_NOTIFY.NAME,
		selectorType = SelectorType.TAG,
		selectorExpression = Constants.QUEUE.PAY_NOTIFY.KEY
)
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class PayNotifyConsumer implements RocketMQListener<MessageExt> {
	@Override
	public void onMessage(MessageExt messageExt) {
		Map<String, String> properties = messageExt.getProperties();
		String jsonStrMsg = new String(messageExt.getBody(), Charset.forName("UTF-8"));
		com.company.framework.messagedriven.rocketmq.utils.ConsumerUtils.handleByStrategy(jsonStrMsg, properties);
	}
}
