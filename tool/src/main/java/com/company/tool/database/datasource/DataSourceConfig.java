package com.company.tool.database.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.company.tool.database.datasource.lookup.RoutingDataSource;

@Configuration
public class DataSourceConfig {
	/**
	 * 配置master数据源
	 */
	@Bean(name = "masterDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.base.master")
	public DataSource masterDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 配置slave1数据源
	 */
	@Bean(name = "slave1DataSource")
	@ConfigurationProperties(prefix = "spring.datasource.base.slave1")
	public DataSource slave1DataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 配置slave2数据源
	 */
	@Bean(name = "slave2DataSource")
	@ConfigurationProperties(prefix = "spring.datasource.base.slave2")
	public DataSource slave2DataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Primary
	@Bean("routingDataSource")
	public DataSource routingDataSource() {
		RoutingDataSource routingDataSource = new RoutingDataSource();

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put(SourceName.MASTER, masterDataSource());
		dataSourceMap.put(SourceName.SLAVE1, slave1DataSource());
		dataSourceMap.put(SourceName.SLAVE2, slave2DataSource());
		routingDataSource.setTargetDataSources(dataSourceMap);

		// 将 master 数据源作为默认指定的数据源
		routingDataSource.setDefaultTargetDataSource(masterDataSource());
		return routingDataSource;
	}
}