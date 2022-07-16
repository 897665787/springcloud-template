package com.company.auth.authentication.impl.tool.impl;

import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IMaTool;
import com.company.auth.authentication.impl.tool.dto.MaSession;
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
	
	@Override
	public MaSession getSessionInfo(String appid, String encryptedData, String iv, String code) {
		MaSession maSession = new MaSession();
		
		final WxMaService wxService = WxMaConfiguration.getMaService(appid);

		// 在调用wx.login之后获得的加密数据，才是新的session_key加密的数据
		try {
			WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
			maSession.setErrcode(0);
			maSession.setSessionKey(session.getSessionKey());
			maSession.setOpenid(session.getOpenid());
			maSession.setUnionid(session.getUnionid());
			// openid与sessionKey的关系
			
			// sessionKey过期处理
			
			// 用户信息校验
//			if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
//				return "user check failed";
//			}

			// 解密
			WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(session.getSessionKey(), encryptedData, iv);
			/**
			手机号
			{
				"phoneNumber": "15220163215",
				"purePhoneNumber": "15220163215",
				"countryCode": "86",
				"watermark": {
					"timestamp": 1614848459,
					"appid": "APPID"
				}
			}
			 */
			
			maSession.setPhoneNumber(phoneNoInfo.getPhoneNumber());
		} catch (WxErrorException e) {
			log.error("getAccessToken error", e);
            WxError error = e.getError();
            maSession.setErrcode(error.getErrorCode());
            maSession.setErrmsg(error.getErrorMsg());
		}
		return maSession;
	}
}
