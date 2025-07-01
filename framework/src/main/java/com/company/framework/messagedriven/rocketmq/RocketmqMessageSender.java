package com.company.framework.messagedriven.rocketmq;

import com.company.framework.util.JsonUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.trace.TraceManager;
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
    @Autowired
    private TraceManager traceManager;

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
        String paramsStr = JsonUtil.toJsonString(toJson);

        Map<String, Object> headers = Maps.newHashMap();
        headers.put(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
        headers.put(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
        headers.put(HeaderConstants.HEADER_MESSAGE_ID, traceManager.get());

        MessageHeaders messageHeaders = new MessageHeaders(headers);
        Message<String> message = MessageBuilder.createMessage(paramsStr, messageHeaders);

        if (StringUtils.isBlank(tag)) {
            tag = "*";
        }
        // destination formats: `topicName:tags`
        String destination = String.format("%s:%s", topic, tag);

        SendResult sendResult;
        if (delaySeconds != null && delaySeconds > 0) {
            int delayLevel = calcDelayLevelBySeconds(delaySeconds);
            sendResult = rocketMQTemplate.syncSend(destination, message, rocketMQTemplate.getProducer().getSendMsgTimeout(), delayLevel);
        } else {
            sendResult = rocketMQTemplate.syncSend(destination, message);
        }
        log.info("syncSend,strategyName:{},toJson:{},topic:{},tag:{},delaySeconds:{},sendResult:{}",
                strategyName, paramsStr, topic, tag, delaySeconds, JsonUtil.toJsonString(sendResult));
    }

    /**
     * 根据延时秒数计算延时级别
     * # broker.conf
     * messageDelayLevel = 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    private int calcDelayLevelBySeconds(int delaySeconds) {
        if (delaySeconds <= 1) {
            return 1;
        }
        if (delaySeconds <= 5) {
            return 2;
        }
        if (delaySeconds <= 10) {
            return 3;
        }
        if (delaySeconds <= 30) {
            return 4;
        }
        if (delaySeconds <= 60) {
            return 5;
        }
        if (delaySeconds <= 120) {
            return 6;
        }
        if (delaySeconds <= 180) {
            return 7;
        }
        if (delaySeconds <= 240) {
            return 8;
        }
        if (delaySeconds <= 300) {
            return 9;
        }
        if (delaySeconds <= 360) {
            return 10;
        }
        if (delaySeconds <= 420) {
            return 11;
        }
        if (delaySeconds <= 480) {
            return 12;
        }
        if (delaySeconds <= 540) {
            return 13;
        }
        if (delaySeconds <= 600) {
            return 14;
        }
        if (delaySeconds <= 1200) {
            return 15;
        }
        if (delaySeconds <= 1800) {
            return 16;
        }
        if (delaySeconds <= 3600) {
            return 17;
        }
        if (delaySeconds <= 7200) {
            return 18;
        }
        return 18;// 最多18个级别
    }
}
