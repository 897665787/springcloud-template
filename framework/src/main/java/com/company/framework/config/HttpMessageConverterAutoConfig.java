package com.company.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.company.common.util.JsonUtil;

@Configuration
public class HttpMessageConverterAutoConfig {

	@Bean
	@ConditionalOnMissingBean(value = MappingJackson2HttpMessageConverter.class)
	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(JsonUtil.mapper());
		return converter;
	}
}
