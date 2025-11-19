package com.company.tool.messagedriven.redis.consumer;

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.tool.messagedriven.strategy.StrategyConstants;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class SendCouponConsumer {

    @Bean
    public MessageListener couponReceiveMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.COUPON_RECEIVE_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerCouponReceiveConsumer(RedisMessageListenerContainer container, MessageListener couponReceiveMessageListener) {
        String channel = FanoutConstants.SEND_COUPON.EXCHANGE;
        container.addMessageListener(couponReceiveMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    @Bean
    public MessageListener couponTouseMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.COUPON_TOUSE_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerCouponTouseConsumer(RedisMessageListenerContainer container, MessageListener couponTouseMessageListener) {
        String channel = FanoutConstants.SEND_COUPON.EXCHANGE;
        container.addMessageListener(couponTouseMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    @Bean
    public MessageListener couponExpireMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.COUPON_EXPIRE_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerCouponExpireConsumer(RedisMessageListenerContainer container, MessageListener couponExpireMessageListener) {
        String channel = FanoutConstants.SEND_COUPON.EXCHANGE;
        container.addMessageListener(couponExpireMessageListener, new ChannelTopic(channel));
        return new Object();
    }
}