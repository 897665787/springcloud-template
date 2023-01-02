package com.company.auth.authentication.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IAliMaTool;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.AliMobile;
import com.company.auth.authentication.impl.tool.dto.AliUserId;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;

@Component(LoginBeanFactory.ALIPAY_MINIAPP)
public class AlipayMiniappService implements LoginService {
	
	@Value("${ali.aesKey:3c4TJ3432fYAFVA44Ry8g==}")
	private String aesKey;
	@Value("${ali.appid:}")
	private String appid;
	@Value("${ali.privateKey:}")
	private String privateKey;
	@Value("${ali.alipayPublicKey:}")
	private String alipayPublicKey;
	
	@Autowired
	private IOauthTool oauthTool;
	@Autowired
	private IAliMaTool aliMaTool;
	
	@Override
	public LoginResult login(String encryptedData, String ignore1, String authcode) {
		if (StringUtils.isBlank(encryptedData)) {
			throw new BusinessException("缺失参数");
		}

		AliMobile aliMobile = aliMaTool.getPhoneNumber(aesKey, encryptedData);
		
		if (!aliMobile.isSuccess()) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage(aliMobile.getMsg());
		}

		String mobile = aliMobile.getMobile();
		if (StringUtils.isBlank(mobile)) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage("授权失败");
		}
		
		Integer userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.MOBILE, mobile).getUserId();
		if (userId == null) {// 账号不存在
			// 创建新用户
			userId = oauthTool.registerUser(mobile, null, null);
		}

		if (StringUtils.isNotBlank(authcode)) {
			AliUserId aliUserId = aliMaTool.getUserId(appid, privateKey, alipayPublicKey, authcode);
			if (aliUserId.isSuccess()) {
				// 绑定用户与支付宝userId关系
				oauthTool.bindOauth(userId, UserOauthEnum.IdentityType.ALI_USERID_MINIAPP, aliUserId.getUserId(),
						authcode);
			}
		}
		
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
