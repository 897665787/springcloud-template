package com.company.framework.messagedriven.redis.aspect;

import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisMQAutoConfiguration;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 日志追踪ID切面
 *
 * @author JQ棣
 */
@Slf4j
@Aspect
@Component
@Conditional(RedisMQAutoConfiguration.RedisMQCondition.class)
@RequiredArgsConstructor
public class TraceAspect {
    private final TraceManager traceManager;

    // 执行之前塞入日志ID，用于追踪整个执行链路
    @Before("execution(* org.springframework.data.redis.connection.MessageListener+.onMessage(org.springframework.data.redis.connection.Message,byte[]))")
    public void setTraceId(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();// 获取方法参数
        String traceId = null;
        for (Object arg : args) {
            if (arg instanceof Message) {
                Message message = (Message) arg;
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);
                traceId = MapUtils.getString(messageMap, HeaderConstants.HEADER_MESSAGE_ID);
                break;
            }
        }
        traceManager.put(traceId);
        log.info("初始化日志ID");
    }

    // 执行完毕之后清理日志ID
    @After("execution(* org.springframework.data.redis.connection.MessageListener+.onMessage(org.springframework.data.redis.connection.Message,byte[]))")
    public void clearTraceId() {
        log.info("清除日志ID");
        traceManager.remove();
    }
}
