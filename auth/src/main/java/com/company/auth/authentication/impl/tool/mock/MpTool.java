package com.company.auth.authentication.impl.tool.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IMpTool;
import com.company.auth.authentication.impl.tool.dto.MpAccessToken;
import com.company.auth.authentication.impl.tool.dto.MpUserInfo;
import com.company.auth.authentication.test.TestData;
import com.company.common.util.JsonUtil;
import com.company.framework.context.Environment;

@Component("mockMpTool")
@Primary
@Profile({ Environment.LOCAL, Environment.DEV, Environment.TEST, Environment.PRE })
@ConditionalOnProperty(prefix = "template.mock", name = "mptool", havingValue = "true")
public class MpTool implements IMpTool {

	@Override
	public MpAccessToken getAccessToken(String appid, String code) {
		String jsonString = TestData.codeOpenidMap.get(code);
		return JsonUtil.toEntity(jsonString, MpAccessToken.class);
	}

	@Override
	public MpUserInfo getUserinfo(String accessToken, String openid) {
		String jsonString = TestData.openidInfoMap.get(openid);
		return JsonUtil.toEntity(jsonString, MpUserInfo.class);
	}
}
