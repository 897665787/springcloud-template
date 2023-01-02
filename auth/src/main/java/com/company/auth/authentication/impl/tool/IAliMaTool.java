package com.company.auth.authentication.impl.tool;

import com.company.auth.authentication.impl.tool.dto.AliMobile;
import com.company.auth.authentication.impl.tool.dto.AliUserId;

public interface IAliMaTool {

	AliMobile getPhoneNumber(String aesKey, String encryptedData);

	AliUserId getUserId(String appid, String privateKey, String alipayPublicKey, String authcode);

}
