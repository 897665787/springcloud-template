package com.company.datasource.mybatis;

import com.company.common.constant.CommonConstants;
import org.mybatis.spring.annotation.MapperScan;

/**
 * MapperScan 配置
 */
//@Configuration 使用org.springframework.boot.autoconfigure.AutoConfiguration.imports装配bean
@MapperScan(basePackages = CommonConstants.BASE_PACKAGE + ".**.mapper")
public class MapperScanAutoConfiguration {
}