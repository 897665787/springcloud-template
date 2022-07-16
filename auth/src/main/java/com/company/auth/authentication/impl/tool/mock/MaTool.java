package com.company.auth.authentication.impl.tool.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IMaTool;
import com.company.auth.authentication.impl.tool.dto.MaSession;
import com.company.framework.context.Environment;

@Component("mockMaTool")
@Primary
@Profile({ Environment.LOCAL, Environment.DEV, Environment.TEST, Environment.PRE })
@ConditionalOnProperty(prefix = "template.mock", name = "matool", havingValue = "true")
public class MaTool implements IMaTool {

	@Override
	public MaSession getSessionInfo(String appid, String encryptedData, String iv, String code) {
		MaSession maSession = new MaSession();
		maSession.setErrcode(0);
		maSession.setSessionKey("11111111111");
		maSession.setOpenid("2222222222222222222222");
		maSession.setUnionid("333333333333333333333");
		maSession.setPhoneNumber("18700873486");
		return maSession;
	}
}
