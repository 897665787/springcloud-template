package com.company.tool.database.mybatisplus.plugins;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.baomidou.mybatisplus.toolkit.PluginUtils;
import com.company.common.util.MdcUtil;
import com.company.common.util.Utils;
import com.company.framework.context.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 给没有添加limit的SQL添加limit，防止全量查询导致慢SQL
 **/
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE) // 此拦截器需要在分页拦截器之后执行，否则可能会导致SQL错误，所以设置最低优先级
@Intercepts(@Signature(type = StatementHandler.class, method = "query", args = { Statement.class,
		ResultHandler.class }))
public class SqlLimitInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 获取sql处理器
		StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
			// 非查询sql
			return invocation.proceed();
		}

		// 获取原始sql
		BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
		String originalSql = boundSql.getSql().toLowerCase();

		int limit = SpringContextUtil.getIntegerProperty("template.sqllimit.max", 9999);
		boolean containsLimit = false;
		// 判断原始的sql是否包含limit
		if (!originalSql.contains(" limit ")) {
			containsLimit = true;
			metaObject.setValue("delegate.rowBounds.limit", limit);
			String id = mappedStatement.getId();
			log.info("==>{},sql has added limit {}", id, limit);
		}
		Object result = invocation.proceed();
		// 判断查询结果是否存在异常
		if (containsLimit && result instanceof List) {
			int size = ((List<?>) result).size();
			String mapperAndId = Utils.mapperAndId(mappedStatement.getId());
			if (limit == size) {
				// 最终结果集数量与最大限制数量一致，可能存在大量数据查询风险
				log.warn("mapper={} may has wrong sql,logId={},sql:{}", mapperAndId, MdcUtil.get(), originalSql);
			}
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