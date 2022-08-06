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

	@Override
	public String generate(String userId, String device) {
		StpUtil.login(userId, device);// 可以做到同端互斥登录
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		log.info("userId:{},device:{},tokenInfo:{}", userId, device, JsonUtil.toJsonString(tokenInfo));
		return tokenInfo.getTokenValue();
	}
	/*
	@Value("${template.enable.access-control:true}")
	private Boolean enableAccessControl;
	
	@Override
	public String checkAndGet(String token) {
		if (StringUtils.isBlank(token)) {
			return null;
		}

		if (!enableAccessControl) {// 关闭访问控制时，不校验token有效性，只获取loginId
			Object loginId = StpUtil.getLoginIdByToken(token);
			return loginId == null ? null : String.valueOf(loginId);
		}
		
		try {
			return StpUtil.getLoginIdAsString();
		} catch (NotLoginException e) {
			log.error("NotLoginException:{},{},{}", e, e.getType(), e.getLoginType(), e.getMessage());
			return null;
		}
	}
	*/
}