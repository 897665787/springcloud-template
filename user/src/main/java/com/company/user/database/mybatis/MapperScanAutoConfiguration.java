package com.company.user.database.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MapperScan 配置
 */
@Configuration
@MapperScan(basePackages = "com.company.user.mapper")
public class MapperScanAutoConfiguration {
}