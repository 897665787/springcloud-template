package com.company.order.database.datasource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.order.database.datasource.SourceName.Slave;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DataSource {
    
	/**
	 * 数据源key值
	 */
	Slave value() default Slave.SLAVE_POLLING;
}
