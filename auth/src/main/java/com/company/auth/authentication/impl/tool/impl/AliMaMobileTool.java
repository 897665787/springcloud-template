package com.company.auth.authentication.impl.tool.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.company.auth.authentication.impl.tool.IAliMaMoibleTool;
import com.company.auth.authentication.impl.tool.dto.AliMobileUserId;
import com.company.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AliMaMobileTool implements IAliMaMoibleTool {
	private static final String PAY_URL = "https://openapi.alipay.com/gateway.do";

	/**
	 * <pre>
	 * 官方文档：https://opendocs.alipay.com/mini/api/openapi-authorize
	 * 前端获取authcode需要使用（auth_user：会员信息授权）
	 * </pre>
	 */
	@Override
	public AliMobileUserId getPhoneNumber(String appid, String privateKey, String alipayPublicKey, String code) {
		try {

			AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
			oauthTokenRequest.setGrantType("authorization_code");
			oauthTokenRequest.setCode(code);

			AlipayClient alipayClient = new DefaultAlipayClient(PAY_URL, appid, privateKey, AlipayConstants.FORMAT_JSON,
					AlipayConstants.CHARSET_UTF8, alipayPublicKey, AlipayConstants.SIGN_TYPE_RSA2);

			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
			log.info("oauthTokenResponse:{}", JsonUtil.toJsonString(oauthTokenResponse));
			
			AliMobileUserId aliMobileUserId = new AliMobileUserId();
			if (!oauthTokenResponse.isSuccess()) {
				aliMobileUserId.setSuccess(false);
				aliMobileUserId.setMsg(oauthTokenResponse.getMsg());
				return aliMobileUserId;
			}

			String accessToken = oauthTokenResponse.getAccessToken();

			AlipayUserInfoShareRequest shareRequest = new AlipayUserInfoShareRequest();
			AlipayUserInfoShareResponse shareResponse = alipayClient.execute(shareRequest, accessToken);
			log.info("shareResponse:{}", JsonUtil.toJsonString(shareResponse));
			
			if (!shareResponse.isSuccess()) {
				aliMobileUserId.setSuccess(false);
				aliMobileUserId.setMsg(oauthTokenResponse.getMsg());
				return aliMobileUserId;
			}
			aliMobileUserId.setSuccess(true);
			aliMobileUserId.setMobile(shareResponse.getMobile());
			aliMobileUserId.setUserId(oauthTokenResponse.getUserId());
			return aliMobileUserId;
		} catch (AlipayApiException e) {
			log.error("getPhoneNumber error", e);
			AliMobileUserId aliMobileUserId = new AliMobileUserId();
			aliMobileUserId.setSuccess(false);
			aliMobileUserId.setMsg(Optional.ofNullable(e.getErrMsg()).orElse(e.getMessage()));
			return aliMobileUserId;
		}
	}
}
