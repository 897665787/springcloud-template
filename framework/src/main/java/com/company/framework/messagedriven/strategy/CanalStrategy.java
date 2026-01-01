package com.company.framework.messagedriven.strategy;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.company.framework.messagedriven.BaseStrategy;
import com.company.framework.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.handler.MessageHandler;

import java.util.Map;

/**
 * canal
 */
@Component(StrategyConstants.CANAL_STRATEGY)
@ConditionalOnProperty(value = "canal.mode", havingValue = "messagedriven")
@RequiredArgsConstructor
public class CanalStrategy implements BaseStrategy<Map<String, Object>> {
    private final MessageHandler<FlatMessage> messageHandler;

    @Override
    public void doStrategy(Map<String, Object> params) {
        FlatMessage message = JsonUtil.toEntity(JsonUtil.toJsonString(params), FlatMessage.class);
        messageHandler.handleMessage(message);
    }
}
