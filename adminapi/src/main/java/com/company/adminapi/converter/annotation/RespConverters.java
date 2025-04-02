package com.company.adminapi.converter.annotation;

import java.lang.annotation.*;

/**
 * 响应实体参数转换
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RespConverters {
	RespConverter[] value();
}