package com.company.framework.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.framework.jackson.serializer.PlusMinusNumberSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = PlusMinusNumberSerializer.class)
public @interface PlusMinusNumber {

	String pattern() default "";
}
