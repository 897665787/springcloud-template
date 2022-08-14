package com.company.gateway.token.satoken.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;

/**
 * [Sa-Token 权限认证] 配置类
 * 
 * @author kong
 */
@Configuration
public class SaTokenConfigure {
	// Sa-Token 整合 jwt (Style模式)
	@Bean
	public StpLogic getStpLogicJwt() {
		return new StpLogicJwtForStateless();
	}
}
