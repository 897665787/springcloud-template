package com.company.datasource.mybatisplus.plugins;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.Properties;

/**
 * 打印SQL耗时
 */
@Slf4j(topic = "LOG_SUMMARY_SQL")
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class SummarySQLInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String sql = null;
        try {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = statementHandler.getBoundSql();
            sql = boundSql.getSql();
        } catch (Exception e) {
            log.error("获取sql语句失败", e);
            sql = "获取sql语句失败";
        }
        // 计算执行 SQL 耗时
        long start = SystemClock.now();
        Object result = invocation.proceed();
        long timing = SystemClock.now() - start;
        log.info("|{}|{}", sql, timing);
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
    }
}
