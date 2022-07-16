package com.company.auth.token.satoken;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.company.auth.token.TokenService;
import com.company.common.util.JsonUtil;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Component
public class SaTokenService implements TokenService {

	public String generate(String userId, String device) {
		StpUtil.login(userId, device);// 可以做到同端互斥登录
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		log.info("userId:{},device:{},tokenInfo:{}", userId, device, JsonUtil.toJsonString(tokenInfo));
		return tokenInfo.getTokenValue();
	}

	public String checkAndGet(String token) {
		return StpUtil.getLoginIdAsString();
	}
}