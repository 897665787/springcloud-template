package com.company.user.messagedriven.rocketmq.consumer;

import com.company.framework.autoconfigure.RocketMQAutoConfiguration;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.rocketmq.utils.ConsumerUtils;
import com.company.user.messagedriven.strategy.StrategyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RocketMQMessageListener(
        topic = FanoutConstants.DEVICE_INFO.EXCHANGE,
        consumerGroup = FanoutConstants.DEVICE_INFO.DEVICE_INFO_RECORD_QUEUE
)
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class DeviceInfoConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        Map<String, String> properties = messageExt.getProperties();
        String jsonStrMsg = new String(messageExt.getBody(), StandardCharsets.UTF_8);

        properties.put(HeaderConstants.HEADER_STRATEGY_NAME, StrategyConstants.DEVICEINFORECORD_STRATEGY);
        ConsumerUtils.handleByStrategy(jsonStrMsg, properties);
    }
}