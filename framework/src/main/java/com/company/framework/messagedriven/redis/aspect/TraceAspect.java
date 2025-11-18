package com.company.framework.messagedriven.redis.aspect;

import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 日志追踪ID切面
 *
 * @author JQ棣
 */
@Slf4j
@Aspect
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class TraceAspect {
    @Autowired
    private TraceManager traceManager;

    // 执行之前塞入日志ID，用于追踪整个执行链路
    @Before("execution(* com.company..messagedriven.redis.consumer.*Consumer.onMessage(..)) && args(message)")
    public void setTraceId(JoinPoint joinPoint, String message) {
        try {
            // 解析消息获取traceId
            Map<String, Object> messageMap = JsonUtil.toEntity(message, Map.class);
            @SuppressWarnings("unchecked")
            Map<String, String> headers = (Map<String, String>) messageMap.get("headers");
            if (headers != null && headers.containsKey(HeaderConstants.HEADER_MESSAGE_ID)) {
                traceManager.put(headers.get(HeaderConstants.HEADER_MESSAGE_ID));
                log.info("初始化日志ID");
                return;
            }
        } catch (Exception e) {
            log.warn("解析消息头失败，使用新的日志ID", e);
        }
        traceManager.put();
        log.info("初始化日志ID");
    }

    // 执行完毕之后清理日志ID
    @After("execution(* com.company..messagedriven.redis.consumer.*Consumer.onMessage(..))")
    public void clearTraceId() {
        log.info("清除日志ID");
        traceManager.remove();
    }
}
