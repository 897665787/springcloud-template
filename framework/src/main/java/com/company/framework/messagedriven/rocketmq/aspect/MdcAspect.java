package com.company.framework.messagedriven.rocketmq.aspect;

import com.company.common.util.MdcUtil;
import com.company.framework.autoconfigure.RocketMQAutoConfiguration;
import com.company.framework.messagedriven.constants.HeaderConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * MDC日志ID切面
 */
@Slf4j
@Aspect
@Component
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
public class MdcAspect {

    // 执行之前塞入日志ID，用于追踪整个执行链路
    @Before("execution(* org.apache.rocketmq.spring.core.RocketMQListener+.onMessage(org.apache.rocketmq.common.message.MessageExt)) && @within(org.apache.rocketmq.spring.annotation.RocketMQMessageListener)")
    public void setMdc(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();// 获取方法参数
        if (args.length == 0 || !(args[0] instanceof MessageExt)) { // 参数不符合预期，拿不到日志ID
            MdcUtil.put();
            log.info("初始化日志ID");
            return;
        }

        MessageExt messageExt = (MessageExt) args[0];
        Map<String, String> properties = messageExt.getProperties();
        MdcUtil.put(properties.get(HeaderConstants.HEADER_MESSAGE_ID));
        log.info("初始化日志ID");
    }

    // 执行完毕之后清理日志ID
    @After("execution(* org.apache.rocketmq.spring.core.RocketMQListener+.onMessage(..)) && @within(org.apache.rocketmq.spring.annotation.RocketMQMessageListener)")
    public void clearMdc() {
        log.info("清除日志ID");
        MdcUtil.remove();
    }
}

