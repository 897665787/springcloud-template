package com.company.datasource.mybatis;

import org.mybatis.spring.annotation.MapperScan;

/**
 * MapperScan 配置
 */
//@Configuration 使用org.springframework.boot.autoconfigure.AutoConfiguration.imports装配bean
@MapperScan(basePackages = "com.company.**.mapper")
public class MapperScanAutoConfiguration {
}
