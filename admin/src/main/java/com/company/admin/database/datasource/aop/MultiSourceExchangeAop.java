package com.company.admin.database.datasource.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.company.admin.database.datasource.SourceName.Slave;
import com.company.admin.database.datasource.annotation.DataSource;
import com.company.admin.database.datasource.lookup.DataSourceContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * 多数据源切换的AOP，切的是 DataSource 注解
 */
@Aspect
@Slf4j
@Component
public class MultiSourceExchangeAop implements Ordered {

	/**
	 * 嵌套使用@DataSource的时候会以最内层的数据源为主,如果有需求DataSourceContextHolder内部需要用栈实现
	 */
	@Around("@annotation(dataSource)")
	public Object around(ProceedingJoinPoint point, DataSource dataSource) throws Throwable {
		Slave slave = dataSource.value();
		String slaveSourceName = slave.getValue().get();
		DataSourceContextHolder.setDataSourceName(slaveSourceName);
		log.info("设置数据源为：{}", slaveSourceName);
		try {
			return point.proceed();
		} finally {
			log.info("清空数据源：{}", DataSourceContextHolder.getDataSourceName());
			DataSourceContextHolder.clearDataSourceName();
		}
	}

	/**
	 * aop的顺序要早于spring的事务
	 */
	@Override
	public int getOrder() {
		return 1;
	}

}
