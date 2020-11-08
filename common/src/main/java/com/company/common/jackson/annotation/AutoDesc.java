package com.company.common.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.common.jackson.serializer.AutoDescJsonSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = AutoDescJsonSerializer.class)
public @interface AutoDesc {
	/**
	 * 枚举class
	 * 
	 * @return
	 */
	Class<? extends Enum<?>> value();

	/**
	 * 对应Class中作为code的属性
	 * 
	 * @return
	 */
	String code() default "code";

	/**
	 * 对应Class中作为desc的属性
	 * 
	 * @return
	 */
	String desc() default "desc";
}
