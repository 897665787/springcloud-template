package com.company.auth.authentication.impl.tool;

import com.company.auth.authentication.impl.tool.dto.MobileBindAuthCode;
import com.company.auth.authentication.impl.tool.dto.UserIdCertificate;
import com.company.user.api.enums.UserOauthEnum;

public interface IOauthTool {
	UserIdCertificate getUserIdCertificate(UserOauthEnum.IdentityType identityType, String identifier);
	
	void bindOauth(Integer userId, UserOauthEnum.IdentityType identityType, String identifier, String certificate);

	void storeMobileBindAuthCode(String authcode, MobileBindAuthCode mobileBindAuthCode);
	
	MobileBindAuthCode getMobileBindAuthCode4Store(String authcode);
	
	Integer registerUser(String mobile, String nickname, String avator);
}
