package com.company.auth.authentication.impl.tool.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IMaTool;
import com.company.auth.authentication.impl.tool.dto.MaSession;
import com.company.auth.authentication.impl.tool.dto.MaSessionPhoneNumber;
import com.company.framework.context.Environment;

@Component("mockMaTool")
@Primary
@Profile({ Environment.LOCAL, Environment.DEV, Environment.TEST, Environment.PRE })
@ConditionalOnProperty(prefix = "template.mock", name = "matool", havingValue = "true")
public class MaTool implements IMaTool {

	@Override
	public MaSession getSessionInfo(String appid, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MaSessionPhoneNumber getSessionInfoAndPhoneNumber(String appid, String encryptedData, String iv,
			String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
