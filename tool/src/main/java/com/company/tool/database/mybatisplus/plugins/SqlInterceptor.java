package com.company.tool.database.mybatisplus.plugins;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class SqlInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = invocation.proceed();
		try {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			String mapperAndId = mappedStatement.getId();
			long end = System.currentTimeMillis();
			log.info("{} cost {}", mapperAndId, end - start);
		} catch (Exception e) {
			log.error("log sql error", e);
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
