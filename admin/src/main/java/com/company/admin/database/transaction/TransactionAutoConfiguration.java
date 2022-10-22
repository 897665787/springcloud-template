package com.company.admin.database.transaction;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务配置
 */
@Configuration
public class TransactionAutoConfiguration {

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("routingDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}