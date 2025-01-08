package com.company.web.amqp.rocketmq.consumer;

import com.company.framework.amqp.rocketmq.utils.ConsumerUtils;
import com.company.framework.autoconfigure.RocketMQAutoConfiguration;
import com.company.web.amqp.Constants;
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
        consumerGroup = "${rocketmq.consumer.group}" + Constants.QUEUE.COMMON.NAME,
        selectorType = SelectorType.TAG,
        selectorExpression = Constants.QUEUE.COMMON.ROUTING_KEY
)
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class CommonConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        Map<String, String> properties = messageExt.getProperties();
        String jsonStrMsg = new String(messageExt.getBody(), Charset.forName("UTF-8"));
        ConsumerUtils.handleByStrategy(jsonStrMsg, properties);
    }
}