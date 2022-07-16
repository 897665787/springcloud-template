package com.company.auth.authentication.impl.tool;

import com.company.auth.authentication.impl.tool.dto.MaSession;

public interface IMaTool {

	public MaSession getSessionInfo(String appid, String encryptedData, String iv, String code);
}
