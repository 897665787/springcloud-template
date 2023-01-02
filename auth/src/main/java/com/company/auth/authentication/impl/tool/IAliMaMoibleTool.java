package com.company.auth.authentication.impl.tool;

import com.company.auth.authentication.impl.tool.dto.AliMobileUserId;

public interface IAliMaMoibleTool {

	AliMobileUserId getPhoneNumber(String appid, String privateKey, String alipayPublicKey, String code);

}
