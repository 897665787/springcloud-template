package com.company.auth.authentication.impl.tool.impl;

import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IMaTool;
import com.company.auth.authentication.impl.tool.dto.MaSession;
import com.company.auth.authentication.impl.tool.dto.MaSessionPhoneNumber;
import com.company.auth.wx.miniapp.config.WxMaConfiguration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;

@Slf4j
@Component
public class MaTool implements IMaTool {

	/**
	 * 特殊使用场景：未登录前端又需要openid、unionid
	 */
	@Override
	public MaSession getSessionInfo(String appid, String code) {
		MaSession maSession = new MaSession();

		final WxMaService wxService = WxMaConfiguration.getMaService(appid);

		// 注意事项：在调用wx.login之后获得的加密数据，才是新的session_key加密的数据
		try {
			/**
			 * 获取sessionKey、openid、unionid
			 * 
			 * <pre>
			 * 官网：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
			 * </pre>
			 */
			WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
			maSession.setErrcode(0);
			maSession.setSessionKey(session.getSessionKey());
			maSession.setOpenid(session.getOpenid());
			maSession.setUnionid(session.getUnionid());
		} catch (WxErrorException e) {
			log.error("getSessionInfo error", e);
			WxError error = e.getError();
			maSession.setErrcode(error.getErrorCode());
			maSession.setErrmsg(error.getErrorMsg());
		}
		return maSession;
	}
	
	@Override
	public MaSessionPhoneNumber getSessionInfoAndPhoneNumber(String appid, String encryptedData, String iv, String code) {
		MaSessionPhoneNumber maSessionPhoneNumber = new MaSessionPhoneNumber();
		
		MaSession maSession = getSessionInfo(appid, code);
		maSessionPhoneNumber.setMaSession(maSession);
		
		Integer errcode = maSession.getErrcode();
		if (errcode != null && errcode != 0) {
			return maSessionPhoneNumber;
		}
		
		/**
		 * 获取手机号
		 * 
		 * <pre>
		 * 官网（旧版获取手机号）：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/deprecatedGetPhoneNumber.html
		 * 
		 * 注意事项：在回调中调用 wx.login 登录，可能会刷新登录态。此时服务器使用 code 换取的 sessionKey 不是加密时使用的 sessionKey，导致解密失败。建议开发者提前进行 login；或者在回调中先使用 checkSession 进行登录态检查，避免 login 刷新登录态。
		 * 
		{
			"phoneNumber": "13580006666",
			"purePhoneNumber": "13580006666",
			"countryCode": "86",
			"watermark": {
				"timestamp": 1614848459,
				"appid": "APPID"
			}
		}
		 * </pre>
		 */
		final WxMaService wxService = WxMaConfiguration.getMaService(appid);
		WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(maSession.getSessionKey(),
				encryptedData, iv);
		maSessionPhoneNumber.setPhoneNumber(phoneNoInfo.getPhoneNumber());
		return maSessionPhoneNumber;
	}
}
