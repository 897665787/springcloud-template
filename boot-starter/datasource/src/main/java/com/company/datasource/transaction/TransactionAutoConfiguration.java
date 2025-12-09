package com.company.datasource.transaction;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务配置
 */
//@Configuration 使用org.springframework.boot.autoconfigure.AutoConfiguration.imports装配bean
public class TransactionAutoConfiguration {

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}