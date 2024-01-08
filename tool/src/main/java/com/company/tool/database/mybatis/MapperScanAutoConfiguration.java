package com.company.tool.database.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MapperScan 配置
 */
@Configuration
@MapperScan(basePackages = "com.company.**.mapper")
public class MapperScanAutoConfiguration {
}