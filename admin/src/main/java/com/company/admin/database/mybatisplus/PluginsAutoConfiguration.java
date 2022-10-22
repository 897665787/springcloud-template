package com.company.admin.database.mybatisplus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.company.admin.database.mybatisplus.plugins.SqlInterceptor;

@Configuration
public class PluginsAutoConfiguration {

	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	@Bean
	public SqlInterceptor sqlInterceptor() {
		return new SqlInterceptor();
	}
}