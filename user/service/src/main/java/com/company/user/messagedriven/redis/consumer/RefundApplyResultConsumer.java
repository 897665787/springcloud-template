package com.company.user.messagedriven.redis.consumer;

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.user.messagedriven.strategy.StrategyConstants;
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
public class RefundApplyResultConsumer {

    @Bean
    public MessageListener memberBuyMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.MEMBER_BUY_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerMemberBuyConsumer(RedisMessageListenerContainer container, MessageListener memberBuyMessageListener) {
        String channel = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE;
        container.addMessageListener(memberBuyMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    @Bean
    public MessageListener goodsRefundMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.GOODS_REFUND_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerGoodsRefundConsumer(RedisMessageListenerContainer container, MessageListener goodsRefundMessageListener) {
        String channel = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE;
        container.addMessageListener(goodsRefundMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    @Bean
    public MessageListener rechargeMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.RECHARGE_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerRechargeConsumer(RedisMessageListenerContainer container, MessageListener rechargeMessageListener) {
        String channel = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE;
        container.addMessageListener(rechargeMessageListener, new ChannelTopic(channel));
        return new Object();
    }
}