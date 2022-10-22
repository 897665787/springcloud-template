package com.company.admin.annotation;

import java.lang.annotation.*;

/**
 * 分页注解，打上该注解的API一定会有分页参数
 * Created by JQ棣 on 2018/06/14.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pagination {
	/** 是否一定要参数中带分页参数 **/
    boolean required() default false;
    
    /** limit 最大值 **/
    long maxLimit() default 100L;
}
