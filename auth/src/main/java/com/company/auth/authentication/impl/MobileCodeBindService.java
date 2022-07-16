package com.company.auth.authentication.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.MobileBindAuthCode;
import com.company.auth.authentication.impl.tool.dto.MobileBindAuthCode.BindUserOauth;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;

@Component(LoginBeanFactory.MOBILE_CODE_BIND)
public class MobileCodeBindService implements LoginService {
	
	@Autowired
	private IOauthTool oauthTool;
	
	@Override
	public LoginResult login(String mobile, String code, String authcode) {
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)) {
			throw new BusinessException("缺失参数");
		}
		
		// 核对验证码
		// select * from bu_verify_code where mobile = '15220163215'
		String verifyCode = "123456";
		if (verifyCode == null || !verifyCode.equals(code)) {
			throw new BusinessException("验证码错误");
		}

		Integer userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.MOBILE, mobile).getUserId();
		
		MobileBindAuthCode mobileBindAuthCode = oauthTool.getMobileBindAuthCode4Store(authcode);
		
		if (userId == null) {// 账号不存在
			// 创建新用户
			String nickname = Optional.ofNullable(mobileBindAuthCode).map(MobileBindAuthCode::getNickname).orElse(null);
			String avator = Optional.ofNullable(mobileBindAuthCode).map(MobileBindAuthCode::getHeadimgurl).orElse(null);

			userId = oauthTool.registerUser(mobile, nickname, avator);
		}

		// 绑定authcode
		if (mobileBindAuthCode != null) {
			List<BindUserOauth> binds = mobileBindAuthCode.getBinds();
			for (BindUserOauth bind : binds) {
				String identifier = bind.getIdentifier();
				if (StringUtils.isNotBlank(identifier)) {
					oauthTool.bindOauth(userId, bind.getIdentityType(), identifier, authcode);
				}
			}
		}
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
