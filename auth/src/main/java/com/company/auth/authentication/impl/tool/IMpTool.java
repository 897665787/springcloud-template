package com.company.auth.authentication.impl.tool;

import com.company.auth.authentication.impl.tool.dto.MpAccessToken;
import com.company.auth.authentication.impl.tool.dto.MpUserInfo;

public interface IMpTool {

	public MpAccessToken getAccessToken(String appid, String code);

	public MpUserInfo getUserinfo(String accessToken, String openid);
}
