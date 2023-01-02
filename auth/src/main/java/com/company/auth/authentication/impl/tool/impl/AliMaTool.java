package com.company.auth.authentication.impl.tool.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.company.auth.authentication.impl.tool.IAliMaTool;
import com.company.auth.authentication.impl.tool.dto.AliMobile;
import com.company.auth.authentication.impl.tool.dto.AliMobileDto;
import com.company.auth.authentication.impl.tool.dto.AliUserId;
import com.company.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AliMaTool implements IAliMaTool {
	private static final String PAY_URL = "https://openapi.alipay.com/gateway.do";
	private static final String SUCCESS_CODE = "10000";
	
	/**
	 * <pre>
	 * 官方文档：https://opendocs.alipay.com/mini/api/getphonenumber
	 * </pre>
	 */
	@Override
	public AliMobile getPhoneNumber(String aesKey, String encryptedData) {
		try {
			/**
			 * <pre>
			 * encryptedData: "d4E0LN/s9c/FWb5fhpReeGCOROpadHaK6mHHDMj1qKyI0S5RwxcNyUB5iaIwAxcZC/TeiDdrYAOvCoQmKskbWw=="
			 * </pre>
			 */
			String mobileContent = AlipayEncrypt.decryptContent(encryptedData, AlipayConstants.ENCRYPT_TYPE_AES,
					aesKey, AlipayConstants.CHARSET_UTF8);
			log.info("mobileContent:{}", mobileContent);
			/**
			 * <pre>
			// 正常结果
			{
				"code": "10000",
				"msg": "Success",
				"mobile": "1597671905"
			}
			
			// 异常结果。解决方案：请参考接入必读第一步
			{
				"code": "40001",
				"msg": "Missing Required Arguments",
				"subCode": "isv.missing-encrypt-key",
				"subMsg": "缺少加密配置"
			}
			
			// 异常结果。解决方案：请参考接入必读第一步
			{
				"code": "40006", // 解决方案：请参考接入必读第二步
				"msg": "Insufficient Permissions",
				"subCode": "isv.insufficient-isv-permissions",
				"subMsg": "ISV权限不足，建议在开发者中心检查对应功能是否已经添加，解决办法详见：https://docs.open.alipay.com/common/isverror"
			}
			
			// 异常结果。解决方案：请参考接入必读第一步
			{
				"code": "40003", // 解决方案：请参考接入必读第三步
				"msg": "Insufficient Conditions",
				"subCode": "isv.invalid-auth-relations",
				"subMsg": "无效的授权关系"
			}
			
			// 异常结果。解决方案：请参考接入必读第一步
			{
				"code": "20000", // 解决方案：请稍后重试
				"msg": "Service Currently Unavailable",
				"subCode": "aop.unknow-error",
				"subMsg": "系统繁忙"
			}
			 * </pre>
			 */
			AliMobileDto aliMobileDto = JsonUtil.toEntity(mobileContent, AliMobileDto.class);
			
			AliMobile aliMobile = new AliMobile();
			if (!SUCCESS_CODE.equals(aliMobileDto.getCode())) {
				aliMobile.setSuccess(false);
				aliMobile.setMsg(Optional.ofNullable(aliMobileDto.getSubMsg()).orElse(aliMobileDto.getMsg()));
				return aliMobile;
			}
			
			aliMobile.setSuccess(true);
			aliMobile.setMobile(aliMobileDto.getMobile());
			return aliMobile;
		} catch (AlipayApiException e) {
			log.error("getMobile error", e);
			AliMobile aliMobile = new AliMobile();
			aliMobile.setSuccess(false);
			aliMobile.setMsg(Optional.ofNullable(e.getErrMsg()).orElse(e.getMessage()));
			return aliMobile;
		}
	}
	
	/**
	 * <pre>
	 * 官方文档：https://opendocs.alipay.com/mini/api/openapi-authorize
	 * 前端获取authcode需要使用（auth_base：基本信息授权）
	 * </pre>
	 */
	@Override
	public AliUserId getUserId(String appid, String privateKey, String alipayPublicKey, String authcode) {
		try {
			AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
			oauthTokenRequest.setGrantType("authorization_code");
			oauthTokenRequest.setCode(authcode);

			AlipayClient alipayClient = new DefaultAlipayClient(PAY_URL, appid, privateKey, AlipayConstants.FORMAT_JSON,
					AlipayConstants.CHARSET_UTF8, alipayPublicKey, AlipayConstants.SIGN_TYPE_RSA2);

			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
			log.info("oauthTokenResponse:{}", JsonUtil.toJsonString(oauthTokenResponse));

			AliUserId aliUserId = new AliUserId();
			if (!oauthTokenResponse.isSuccess()) {
				aliUserId.setSuccess(false);
				aliUserId.setMsg(oauthTokenResponse.getMsg());
				return aliUserId;
			}

			aliUserId.setSuccess(true);
			aliUserId.setUserId(oauthTokenResponse.getUserId());
			return aliUserId;
		} catch (AlipayApiException e) {
			log.error("getUserId error", e);
			AliUserId aliUserId = new AliUserId();
			aliUserId.setSuccess(false);
			aliUserId.setMsg(Optional.ofNullable(e.getErrMsg()).orElse(e.getMessage()));
			return aliUserId;
		}
	}
}
