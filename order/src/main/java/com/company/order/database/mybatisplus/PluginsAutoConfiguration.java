package com.company.order.database.mybatisplus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.company.order.database.mybatisplus.plugins.SqlLimitInterceptor;

@Configuration
public class PluginsAutoConfiguration {

	/**
	 * 分页拦截器
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * <pre>
	 * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
	 * 
	 * 结合logback-conf.xml 
	 * <logger name="com.baomidou.mybatisplus.plugins.PerformanceInterceptor" level="DEBUG" additivity="false">
	 * 输出日志
	 * </pre>
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
		performanceInterceptor.setWriteInLog(true);
		return performanceInterceptor;
	}

	/**
	 * 给没有添加limit的SQL添加limit，防止全量查询导致慢SQL
	 **/
	@Bean
	@ConditionalOnProperty(prefix = "template.sqllimit", name = "enable", havingValue = "true")
	public SqlLimitInterceptor sqlLimitInterceptor() {
		return new SqlLimitInterceptor();
	}
}