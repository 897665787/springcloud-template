package com.company.user.database.mybatisplus;

import javax.sql.DataSource;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusProperties;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import com.company.user.database.mybatisplus.plugins.SqlInterceptor;

/**
 * TODO 多主库的情况下需要一对一配置SqlSessionFactory
 * 
 * MybatisPlus 配置
 */
//@Configuration
//@MapperScan(basePackages = "com.company.user.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
//@EnableConfigurationProperties(MybatisPlusProperties.class)
public class MybatisPlusAutoConfiguration {

	@Bean("sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("routingDataSource") DataSource dataSource,
			MybatisPlusProperties properties) throws Exception {
		MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);

		factory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*Mapper.xml"));
		factory.setTypeAliasesPackage("com.company.*.entity");
		
		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setCacheEnabled(false);// 使全局的映射器启用或禁用缓存
		configuration.setLazyLoadingEnabled(true);// 全局启用或禁用延迟加载。当禁用时，所有关联对象都会即时加载
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setMapUnderscoreToCamelCase(true);// 使用驼峰命名法转换字段
		configuration.setJdbcTypeForNull(JdbcType.NULL);// 设置但JDBC类型为空时,某些驱动程序要指定值,default:OTHER，插入空值时不需要指定类型
		configuration.setLogImpl(StdOutImpl.class); // 打印查询语句
		factory.setConfiguration(configuration);

		
		GlobalConfiguration globalConfig = new GlobalConfiguration();
		globalConfig.setDbType(DBType.MYSQL.getDb());
		globalConfig.setIdType(IdType.AUTO.getKey());
		globalConfig.setFieldStrategy(FieldStrategy.NOT_EMPTY.getKey());
		globalConfig.setDbColumnUnderline(true);
		globalConfig.setRefresh(true);// 是否刷新mapper
		// globalConfig.setLogicDeleteValue("1");
		// globalConfig.setLogicNotDeleteValue("0");
		// globalConfig.setMetaObjectHandler(new DefaultMetaObjectHandler());
		// globalConfig.setSqlInjector(new LogicSqlInjector());
		factory.setGlobalConfig(globalConfig);

		factory.setPlugins(new Interceptor[] { new PaginationInterceptor(), new SqlInterceptor() });

		return factory.getObject();
	}

	@Bean(name = "sqlSessionTemplate")
	public SqlSessionTemplate SqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}