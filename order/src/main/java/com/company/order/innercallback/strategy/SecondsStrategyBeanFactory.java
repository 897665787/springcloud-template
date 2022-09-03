package com.company.order.innercallback.strategy;

import com.company.framework.context.SpringContextUtil;
import com.company.order.enums.InnerCallbackEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecondsStrategyBeanFactory {
	private static final String SUFFIX = "SecondsStrategy";

	public static final String INCREMENT_SECONDSSTRATEGY = InnerCallbackEnum.SECONDSSTRATEGY_INCREMENT
			+ SUFFIX;
	public static final String FIX_SECONDSSTRATEGY = InnerCallbackEnum.SECONDSSTRATEGY_FIX + SUFFIX;

	public static SecondsStrategy of(String increaseStrategy) {
		String beanName = increaseStrategy + SUFFIX;
		SecondsStrategy secondsStrategy = SpringContextUtil.getBean(beanName,
				SecondsStrategy.class);
		if (secondsStrategy == null) {
			log.warn("秒数增加策略{}找不到，使用默认策略执行", increaseStrategy);
			// 默认策略
			secondsStrategy = SpringContextUtil.getBean(INCREMENT_SECONDSSTRATEGY,
					SecondsStrategy.class);
		}
		return secondsStrategy;
	}
}
