package com.company.framework.autoconfigure;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.company.framework.redis.RedisHolder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CustomRedisAutoConfiguration {
	
//	@Bean
//	public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
//		RedisTemplate<String, Serializable> template = new RedisTemplate<>();
//		template.setKeySerializer(new StringRedisSerializer());
//		
//		ObjectMapper mapper = new ObjectMapper();
//		// 忽略null值
//		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		// 时间类型数据格式化
//		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//		// 遇未知属性不报错
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		// 允许使用非双引号属性名字
//		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//		// 允许一个没有属性的类被序列化
//		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//		// 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
//		
//		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(mapper);
//		template.setValueSerializer(genericJackson2JsonRedisSerializer);
//		template.setConnectionFactory(redisConnectionFactory);
//		return template;
//	}
	
	@Bean
	public RedisHolder redisHolder() {
		return new RedisHolder();
	}
}