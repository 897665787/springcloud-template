package com.company.job.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.company.common.util.MdcUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * MDC日志ID切面
 * 
 * @author JQ棣
 */
@Slf4j
@Aspect
@Component
public class MdcAspect {

	// 在jobHander执行之前塞入日志ID，用于追踪整个执行链路
	@Before("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
	public void setMdc() {
		MdcUtil.put();
		log.info("初始化日志ID");
	}

	// 执行完毕之后清理日志ID
	@After("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
	public void clearMdc() {
		log.info("清除日志ID");
		MdcUtil.remove();
	}

}
