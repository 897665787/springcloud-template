package com.company.admin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务注解
 * Created by xuxiaowei on 2017/12/27.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
public @interface XSTransactional {

	@AliasFor(annotation = Transactional.class)
	Class<? extends Throwable>[] rollbackFor() default { Exception.class };

}
