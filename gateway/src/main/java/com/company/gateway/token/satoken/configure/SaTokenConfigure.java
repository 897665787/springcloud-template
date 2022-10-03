package com.company.gateway.token.satoken.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.util.SaResult;

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

	/**
	 * 注册 [Sa-Token全局过滤器]
	 */
	@Bean
	public SaReactorFilter getSaReactorFilter() {
		// https://sa-token.dev33.cn/doc/index.html#/start/webflux-example
		return new SaReactorFilter()
				// 指定 [拦截路由]
				.addInclude("/**")
				// 指定 [放行路由]
				.addExclude("/favicon.ico")
				// 指定[认证函数]: 每次请求执行
				.setAuth(obj -> {
					System.out.println("---------- sa全局认证");
					// SaRouter.match("/test/test", () -> StpUtil.checkLogin());
				})
				// 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数
				.setError(e -> {
					System.out.println("---------- sa全局异常 ");
					return SaResult.error(e.getMessage());
				});
	}
}
