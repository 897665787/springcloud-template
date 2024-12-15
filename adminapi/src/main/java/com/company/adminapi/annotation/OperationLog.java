package com.company.adminapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.adminapi.enums.OperationLogEnum;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

	/**
	 * 模块
	 */
	String title() default "";

	/**
	 * 功能
	 */
	OperationLogEnum.BusinessType businessType() default OperationLogEnum.BusinessType.OTHER;

	/**
	 * 是否保存请求的参数
	 */
	boolean isSaveRequestData() default true;

	/**
	 * 是否保存响应的参数
	 */
	boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}
