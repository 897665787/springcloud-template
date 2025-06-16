package com.company.gateway.messagedriven.springevent.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 日志追踪ID切面
 */
@Slf4j
@Aspect
//@Component // springevent使用的是线程池执行，配置在AsyncExecutorConfig，线程池已经处理了日志ID的传递
@ConditionalOnProperty(prefix = "template.enable", name = "message-driven", havingValue = "springevent")
public class TraceAspect {
}
