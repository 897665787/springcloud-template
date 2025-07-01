package com.company.framework.deploy.messagedriven.rocketmq.consumer;

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.rocketmq.utils.ConsumerUtils;
import com.company.framework.messagedriven.rocketmq.RocketMQAutoConfiguration;
import com.company.framework.deploy.messagedriven.strategy.StrategyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RocketMQMessageListener(
        topic = FanoutConstants.DEPLOY.EXCHANGE,
        consumerGroup = FanoutConstants.DEPLOY.EXCHANGE + "-${spring.application.name}",
        messageModel = MessageModel.BROADCASTING
)
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class RefreshConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        Map<String, String> properties = messageExt.getProperties();
        String jsonStrMsg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        properties.put(HeaderConstants.HEADER_STRATEGY_NAME, StrategyConstants.REFRESH_STRATEGY);
        ConsumerUtils.handleByStrategy(jsonStrMsg, properties);
    }
}
