package com.company.zuul.token.satoken.configure;

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
		// https://sa-token.dev33.cn/doc/index.html#/plugin/jwt-extend
		// return new StpLogicJwtForSimple();// Token风格替换，数据记录到redis
		// return new StpLogicJwtForMixin();// jwt 与 Redis 逻辑混合
		return new StpLogicJwtForStateless();// 完全舍弃Redis，只用jwt
	}
}
