package com.company.framework.context;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringContextUtil implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private final static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);

	private static ApplicationContext context;

	private SpringContextUtil() {
	}
	
	public static SpringContextUtil newInstance() {
		return new SpringContextUtil();
	}
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		context = applicationContext;
		logger.info("initialize applicationContext");
	}

	public static ApplicationContext getContext() {
		return context;
	}

	/// 获取当前环境
	public static String getActiveProfile() {
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
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return context.getBean(name, requiredType);
	}

	public static String getProperty(String key, String defaultValue) {
		return context.getEnvironment().getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return context.getEnvironment().getProperty(key);
	}

	public static int getIntegerProperty(String key, int defaultValue) {
		return Optional.ofNullable(getProperty(key)).map(Integer::valueOf).orElse(defaultValue);
	}

}
