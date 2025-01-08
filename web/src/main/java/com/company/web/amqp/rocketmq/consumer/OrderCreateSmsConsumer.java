package com.company.web.amqp.rocketmq.consumer;

import com.company.framework.amqp.constants.FanoutConstants;
import com.company.framework.amqp.constants.HeaderConstants;
import com.company.framework.amqp.rocketmq.utils.ConsumerUtils;
import com.company.framework.autoconfigure.RocketMQAutoConfiguration;
import com.company.web.amqp.strategy.StrategyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;

@Component
@RocketMQMessageListener(
        topic = FanoutConstants.ORDER_CREATE.EXCHANGE,
        consumerGroup = "${rocketmq.consumer.group}-" + FanoutConstants.ORDER_CREATE.SMS_QUEUE
)
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class OrderCreateSmsConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        Map<String, String> properties = messageExt.getProperties();
        String jsonStrMsg = new String(messageExt.getBody(), Charset.forName("UTF-8"));

        properties.put(HeaderConstants.HEADER_STRATEGY_NAME, StrategyConstants.ORDERCREATE_SMS_STRATEGY);
        ConsumerUtils.handleByStrategy(jsonStrMsg, properties);
    }
}