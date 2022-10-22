package com.company.admin.annotation;

import java.lang.annotation.*;

/**
 * @author xxw
 * @date 2018/9/26
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    String value();
}
