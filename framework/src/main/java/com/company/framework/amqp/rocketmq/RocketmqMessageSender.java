package com.company.framework.amqp.rocketmq;

import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.constants.HeaderConstants;
import com.company.framework.autoconfigure.RocketMQAutoConfiguration;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class RocketmqMessageSender implements MessageSender {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendNormalMessage(String strategyName, Object toJson, String topic, String tag) {
        sendMessage(strategyName, toJson, topic, tag, null);
    }

    @Override
    public void sendFanoutMessage(Object toJson, String topic) {
        sendMessage(null, toJson, topic, null, null);
    }

    @Override
    public void sendDelayMessage(String strategyName, Object toJson, String topic, String tag, Integer delaySeconds) {
        sendMessage(strategyName, toJson, topic, tag, delaySeconds);
    }

    /**
     * 发送消息
     *
     * @param strategyName
     * @param toJson
     * @param topic
     * @param tag
     * @param delaySeconds
     */
    private void sendMessage(String strategyName, Object toJson, String topic, String tag,
                             Integer delaySeconds) {
        if (rocketMQTemplate == null) {
            log.warn("rocketMQTemplate not init");
            return;
        }

        String paramsStr = JsonUtil.toJsonString(toJson);

        Map<String, Object> headers = Maps.newHashMap();
        headers.put(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
        headers.put(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
        headers.put("message_id", MdcUtil.get());

        MessageHeaders messageHeaders = new MessageHeaders(headers);
        Message<String> message = MessageBuilder.createMessage(paramsStr, messageHeaders);

        if (StringUtils.isBlank(tag)) {
            tag = "*";
        }
        // destination formats: `topicName:tags`
        String destination = String.format("%s:%s", topic, tag);

        SendResult sendResult;
        if (delaySeconds != null && delaySeconds > 0) {
            sendResult = rocketMQTemplate.syncSendDelayTimeSeconds(destination, message, delaySeconds);
        } else {
            sendResult = rocketMQTemplate.syncSend(destination, message);
        }
        log.info("syncSend,strategyName:{},toJson:{},topic:{},tag:{},delaySeconds:{},sendResult:{}",
                strategyName, paramsStr, topic, tag, delaySeconds, JsonUtil.toJsonString(sendResult));
    }
}