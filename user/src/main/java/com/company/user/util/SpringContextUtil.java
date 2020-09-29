package com.company.user.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

	private final static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.context = applicationContext;
	}

	public static ApplicationContext getContext() {
		assertApplicationContext();
		return context;
	}

	/// 获取当前环境
	private static String getActiveProfile() {
		String profile = context.getEnvironment().getActiveProfiles()[0];
		logger.info(">>>当前环境变量为...{}", profile);
		return profile;
	}

	/**
	 * 测试环境
	 * 
	 * @return
	 */
	public static Boolean isTestProfile() {
		return false;
	}

	/**
	 * 生产环境
	 * 
	 * @return
	 */
	public static Boolean isProduceProfile() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertApplicationContext();
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		assertApplicationContext();
		return context.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return context.getBean(name, requiredType);
	}

	private static void assertApplicationContext() {
		if (SpringContextUtil.context == null) {
			throw new RuntimeException("context属性为null,请检查是否注入了SpringContextUtil!");
		}
	}

	public static String getProperty(String key, String defaultValue) {
		return context.getEnvironment().getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return context.getEnvironment().getProperty(key);
	}

}
