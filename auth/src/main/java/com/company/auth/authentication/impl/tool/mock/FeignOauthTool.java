package com.company.auth.authentication.impl.tool.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.MobileBindAuthCode;
import com.company.auth.authentication.impl.tool.dto.UserIdCertificate;
import com.company.auth.authentication.test.TestData;
import com.company.auth.authentication.test.TestData.Oauth;
import com.company.auth.authentication.test.TestData.UserInfo;
import com.company.common.util.JsonUtil;
import com.company.framework.context.Environment;
import com.company.user.api.enums.UserOauthEnum;

@Component("mockFeignOauthTool")
@Primary
@Profile({ Environment.LOCAL, Environment.DEV, Environment.TEST, Environment.PRE })
@ConditionalOnProperty(prefix = "template.mock", name = "oauthtool", havingValue = "true")
public class FeignOauthTool implements IOauthTool {
	
	@Override
	public UserIdCertificate getUserIdCertificate(UserOauthEnum.IdentityType identityType, String identifier) {
		Oauth oauth = TestData.selectOauth(identifier, identityType.getCode());
		if (oauth == null) {
			return new UserIdCertificate();
		}
		return new UserIdCertificate().setUserId(oauth.getUserId()).setCertificate(oauth.getCertificate());
	}

	@Override
	public void bindOauth(Integer userId, UserOauthEnum.IdentityType identityType, String identifier, String certificate) {
		TestData.bindOauth(userId, identityType.getCode(), identifier, certificate);
	}

	@Override
	public void storeMobileBindAuthCode(String authcode, MobileBindAuthCode mobileBindAuthCode) {
		TestData.redis.put("authcode:" + authcode, JsonUtil.toJsonString(mobileBindAuthCode));
	}

	@Override
	public MobileBindAuthCode getMobileBindAuthCode4Store(String authcode) {
		String string = TestData.redis.get("authcode:" + authcode);
		MobileBindAuthCode mobileBindAuthCode = JsonUtil.toEntity(string, MobileBindAuthCode.class);
		return mobileBindAuthCode;
	}

	@Override
	public Integer registerUser(String mobile, String nickname, String avator) {
		UserInfo userInfo = TestData.registerUser(nickname, avator);
		Integer userId = userInfo.getId();
		TestData.bindOauth(userId, UserOauthEnum.IdentityType.MOBILE.getCode(), mobile, null);
		return userId;
	}
}
