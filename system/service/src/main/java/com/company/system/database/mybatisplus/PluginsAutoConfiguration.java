package com.company.system.database.mybatisplus;

import com.company.system.database.mybatisplus.plugins.SummarySQLInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.company.framework.context.SpringContextUtil;
import com.company.system.database.mybatisplus.plugins.PerformanceInterceptor;
import com.company.system.database.mybatisplus.plugins.SqlLimitInterceptor;

@Configuration
public class PluginsAutoConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		// 分页拦截器
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		
		// 给没有添加limit的SQL添加limit，防止全量查询导致慢SQL
		int limit = SpringContextUtil.getIntegerProperty("template.sqllimit.max", 0);
		if (limit > 0) {
			mybatisPlusInterceptor.addInnerInterceptor(new SqlLimitInterceptor(limit));
		}
		return mybatisPlusInterceptor;
	}

	/**
	 * <pre>
	 * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
	 * 
	 * 结合logback-conf.xml 
	 * <logger name="com.company.system.database.mybatisplus.plugins.PerformanceInterceptor" level="DEBUG" additivity="false">
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
	 * <pre>
	 * 打印SQL耗时
	 *
	 * 结合logback-conf-summary.xml
	 * LOG_SUMMARY_SQL
	 * 输出日志
	 * </pre>
	 */
	@Bean
	public SummarySQLInterceptor summarySQLInterceptor() {
		return new SummarySQLInterceptor();
	}
}