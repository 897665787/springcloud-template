package com.company.order.database.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MapperScan 配置
 */
@Configuration
@MapperScan(basePackages = "com.company.order.mapper")
public class MapperScanAutoConfiguration {
}