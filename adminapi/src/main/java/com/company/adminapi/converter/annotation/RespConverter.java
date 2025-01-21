package com.company.adminapi.converter.annotation;

import com.company.adminapi.converter.ds.ConverterDataSource;
import com.company.adminapi.converter.ds.DefaultConverterDataSource;

import java.lang.annotation.*;

/**
 * 响应实体参数转换
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Repeatable(RespConverters.class)
public @interface RespConverter {
	/**
	 * 字段名
	 */
	String field();

	/**
	 * 新字段名，为空则覆盖原有字段值
	 */
	String newField() default "";

	/**
	 * 数据源
	 */
	Class<? extends ConverterDataSource> dataSource() default DefaultConverterDataSource.class;
}