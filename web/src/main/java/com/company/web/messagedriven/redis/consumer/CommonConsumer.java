package com.company.web.messagedriven.redis.consumer;

import com.company.framework.autoconfigure.RocketMQAutoConfiguration;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.MessageStreamKey;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class CommonConsumer implements StreamListener<MessageStreamKey, ObjectRecord<MessageStreamKey, String>> {

    @Override
    public void onMessage(ObjectRecord<MessageStreamKey, String> message) {
        RecordId recordId = message.getId();
        Long sequence = recordId.getSequence();
        String value = recordId.getValue();
        Long timestamp = recordId.getTimestamp();

        MessageStreamKey streamKey = message.getStream();
        String jsonStrMsg = message.getValue();

        assert streamKey != null;

        Map<String, Object> headers = streamKey.getHeaders();
        String strategyName = MapUtils.getString(headers, HeaderConstants.HEADER_STRATEGY_NAME);
        if (StringUtils.isBlank(strategyName)) {
            return;
        }
        String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
        ConsumerUtils.handleByStrategy(jsonStrMsg, strategyName, paramsClassName);

    }
}
