package com.company.framework.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.framework.jackson.serializer.FormatNumberSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.RoundingMode;

/**
 * 将数值格式化
 *
 * @author JQ棣
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = FormatNumberSerializer.class)
public @interface FormatNumber {

	String pattern() default "";

    RoundingMode roundingMode() default RoundingMode.HALF_EVEN;// 跟DecimalFormat默认值保持一致
}
