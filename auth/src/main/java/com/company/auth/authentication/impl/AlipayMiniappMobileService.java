package com.company.auth.authentication.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IAliMaMoibleTool;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.AliMobileUserId;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;

@Component(LoginBeanFactory.ALIPAY_MINIAPP_MOBILE)
public class AlipayMiniappMobileService implements LoginService {
	
	@Value("${ali.appid:}")
	private String appid;
	@Value("${ali.privateKey:}")
	private String privateKey;
	@Value("${ali.alipayPublicKey:}")
	private String alipayPublicKey;
	
	@Autowired
	private IOauthTool oauthTool;
	@Autowired
	private IAliMaMoibleTool aliMaMoibleTool;

	@Override
	public LoginResult login(String ignore1, String ignore2, String authcode) {
		if (StringUtils.isBlank(authcode)) {
			throw new BusinessException("缺失参数");
		}

		AliMobileUserId aliMobileUserId = aliMaMoibleTool.getPhoneNumber(appid, privateKey, alipayPublicKey, authcode);
		
		if (!aliMobileUserId.isSuccess()) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage(aliMobileUserId.getMsg());
		}

		String mobile = aliMobileUserId.getMobile();
		if (StringUtils.isBlank(mobile)) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage("授权失败");
		}
		
		Integer userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.MOBILE, mobile).getUserId();
		if (userId == null) {// 账号不存在
			// 创建新用户
			userId = oauthTool.registerUser(mobile, null, null);
		}
		
		// 绑定用户与支付宝userId关系
		oauthTool.bindOauth(userId, UserOauthEnum.IdentityType.ALI_USERID_MINIAPP, aliMobileUserId.getUserId(), authcode);
		
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
