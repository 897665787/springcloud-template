package com.company.auth.authentication.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.common.exception.BusinessException;

@Component(LoginBeanFactory.LOCAL_MOBILE)
public class LocalMobileService implements LoginService {
	
	@Autowired
	private IOauthTool oauthTool;
	
	@Override
	public LoginResult login(String ignore1, String ignore2, String authcode) {
		if (StringUtils.isBlank(authcode)) {
			throw new BusinessException("缺失参数");
		}
		// 通过3大运营商获取本机手机号码，可直接使用
		
		String mobile = "15220163215";
		
		Integer userId = oauthTool.registerUser(mobile, null, null);
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
