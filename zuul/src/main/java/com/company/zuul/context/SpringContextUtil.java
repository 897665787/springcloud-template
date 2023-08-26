package com.company.zuul.context;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		String profile = getActiveProfile();
		logger.info(">>>当前环境变量为...{}", profile);
	}

	public static ApplicationContext getContext() {
		return context;
	}

	/// 获取当前环境
	public static String getActiveProfile() {
		String profile = context.getEnvironment().getProperty("spring.profiles.active");
		return profile;
	}

	/**
	 * 测试环境
	 * 
	 * @return
	 */
	public static Boolean isTestProfile() {
		String profile = getActiveProfile();
		return Environment.TEST_ENVIRONMENT.contains(profile);
	}

	/**
	 * 生产环境
	 * 
	 * @return
	 */
	public static Boolean isProduceProfile() {
		String profile = getActiveProfile();
		return Environment.PRODUCE_ENVIRONMENT.contains(profile);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		try {
			return context.getBean(name, requiredType);
		} catch (BeansException e) {
			log.error("has not been", e);
		}
		return null;
	}
	
	public static <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
		return context.getBeansOfType(requiredType);
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
