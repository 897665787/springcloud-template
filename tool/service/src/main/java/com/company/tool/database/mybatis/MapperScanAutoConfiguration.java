package com.company.tool.database.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

import com.company.framework.constant.CommonConstants;

/**
 * MapperScan 配置
 */
@Configuration
@MapperScan(basePackages = CommonConstants.BASE_PACKAGE + ".**.mapper")
public class MapperScanAutoConfiguration {
}
