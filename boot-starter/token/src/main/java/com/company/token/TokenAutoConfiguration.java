package com.company.token;

import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;
import com.company.token.satoken.SaTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

//@Configuration 使用org.springframework.boot.autoconfigure.AutoConfiguration.imports装配bean
public class TokenAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TokenService tokenService() {
//        TokenService tokenService = new JsonWebTokenService();
        TokenService tokenService = new SaTokenService();
        return tokenService;
	}

    // Sa-Token 整合 jwt (Style模式)
    @Bean // 仅使用SaToken需要
    public StpLogic getStpLogicJwt() {
        // https://sa-token.dev33.cn/doc/index.html#/plugin/jwt-extend
//		return new StpLogicJwtForSimple();// Token风格替换，数据记录到redis
//		return new StpLogicJwtForMixin();// jwt 与 Redis 逻辑混合
        return new StpLogicJwtForStateless();// 完全舍弃Redis，只用jwt
    }
}