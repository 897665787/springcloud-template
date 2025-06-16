package com.company.gateway.messagedriven.rabbitmq.aspect;

import com.company.gateway.autoconfigure.RabbitMQAutoConfiguration;
import com.company.gateway.trace.TraceManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * 日志追踪ID切面
 */
@Slf4j
@Aspect
@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class TraceAspect {

    @Autowired
    private TraceManager traceManager;

    // 执行之前塞入日志ID，用于追踪整个执行链路
    @Before("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public void setTraceId(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();// 获取方法参数
        if (args.length == 0 || !(args[0] instanceof Message)) { // 参数不符合预期，拿不到日志ID
            traceManager.put();
            log.info("初始化日志ID");
            return;
        }

        Message message = (Message) args[0];
        MessageProperties messageProperties = message.getMessageProperties();
        traceManager.put(messageProperties.getMessageId());
        log.info("初始化日志ID");
    }

    // 执行完毕之后清理日志ID
    @After("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public void clearTraceId() {
        log.info("清除日志ID");
        traceManager.remove();
    }
}

