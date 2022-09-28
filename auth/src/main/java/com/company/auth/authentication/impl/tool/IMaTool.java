package com.company.auth.authentication.impl.tool;

import com.company.auth.authentication.impl.tool.dto.MaSession;
import com.company.auth.authentication.impl.tool.dto.MaSessionPhoneNumber;

public interface IMaTool {
	
	public MaSession getSessionInfo(String appid, String code);
	
	public MaSessionPhoneNumber getSessionInfoAndPhoneNumber(String appid, String encryptedData, String iv, String code);
}
