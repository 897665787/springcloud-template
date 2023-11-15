package com.company.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * classpath工具类
 */
@Slf4j
public class ClasspathUtil {

	private ClasspathUtil() {
	}

	/**
	 * 读取classpath下面的文件内容（解决读取不到jar包中的文件问题）
	 * 
	 * @param filePath
	 * @return 文件字符串内容
	 */
	public static String readFileAsString(String filePath) {
		if (!filePath.startsWith(ResourceLoader.CLASSPATH_URL_PREFIX)) {
			filePath = ResourceLoader.CLASSPATH_URL_PREFIX + filePath;
		}
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(filePath);
		try (InputStream inputStream = resource.getInputStream()) {
			String content = IoUtil.read(inputStream, Charset.forName("UTF-8"));
			log.info("read {},content:{}", resource.getDescription(), content);
			return content;
		} catch (IOException e) {
			log.error("IOException", e);
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// String filePath = "classpath:application-apollo-dev.yml";
		String filePath = "application-apollo-dev.yml";
		String readString = ClasspathUtil.readFileAsString(filePath);
		System.out.println("readString: " + readString);
	}
}
