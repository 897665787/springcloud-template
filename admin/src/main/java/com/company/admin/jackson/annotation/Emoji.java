package com.company.admin.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.admin.jackson.serializer.EmojiSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * <p>
 * Emoji注解，用于转换包含emoji表情的字符串
 * </p>
 * <b>用法</b><br>
 * 入参：将注解打在Controller的方法，方法内的参数和对应的属性上 <br>
 * 返回值：json,打在对应的属性上；web response,使用标签${xs:reverseEmoji()}
 * {@link EmojiTag}<br>
 * 
 * Created by JQ棣 on 2018/06/14.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = EmojiSerializer.class)
public @interface Emoji {

}
