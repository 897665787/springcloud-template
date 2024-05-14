package com.company.framework.context;

import java.util.Collections;
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
		if (context == null) {
			return null;
		}
		String profile = context.getEnvironment().getProperty("spring.profiles.active");
		return profile;
	}

	/**
	 * 测试环境
	 * 
	 * @return
	 */
	public static boolean isTestProfile() {
		String profile = getActiveProfile();
		if (profile == null) {
			return true;
		}
		return Environment.TEST_ENVIRONMENT.contains(profile);
	}

	/**
	 * 生产环境
	 * 
	 * @return
	 */
	public static boolean isProduceProfile() {
		String profile = getActiveProfile();
		if (profile == null) {
			return false;
		}
		return Environment.PRODUCE_ENVIRONMENT.contains(profile);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		if (context == null) {
			return null;
		}
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		if (context == null) {
			return null;
		}
		return context.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		if (context == null) {
			return null;
		}
		try {
			return context.getBean(name, requiredType);
		} catch (BeansException e) {
			log.error("has not been", e);
		}
		return null;
	}
	
	public static <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
		if (context == null) {
			return Collections.emptyMap();
		}
		return context.getBeansOfType(requiredType);
	}

	public static String getProperty(String key, String defaultValue) {
		if (context == null) {
			return defaultValue;
		}
		return context.getEnvironment().getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		if (context == null) {
			return null;
		}
		return context.getEnvironment().getProperty(key);
	}

	public static int getIntegerProperty(String key, int defaultValue) {
		return Optional.ofNullable(getProperty(key)).map(Integer::parseInt).orElse(defaultValue);
	}
	
	public static boolean getBooleanProperty(String key, boolean defaultValue) {
		return Optional.ofNullable(getProperty(key)).map(Boolean::parseBoolean).orElse(defaultValue);
	}
}
