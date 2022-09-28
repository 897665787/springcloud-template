package com.company.auth.authentication.impl.tool.impl;

import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IMaMobileTool;
import com.company.auth.authentication.impl.tool.dto.MaMobile;
import com.company.auth.wx.miniapp.config.WxMaConfiguration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;

@Slf4j
@Component
public class MaMobileTool implements IMaMobileTool {

	@Override
	public MaMobile getMobileInfo(String appid, String code) {
		MaMobile maMobile = new MaMobile();

		final WxMaService wxService = WxMaConfiguration.getMaService(appid);

		try {
			/**
			 * 获取手机号
			 * 
			 * <pre>
			 * 官网（新版获取手机号）：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/getPhoneNumber.html
			 * 
			{
				"errcode": 0,
				"errmsg": "ok",
				"phone_info": {
					"phoneNumber": "13580006666",
					"purePhoneNumber": "13580006666",
					"countryCode": 86,
					"watermark": {
						"timestamp": 1614848459,
						"appid": "APPID"
					}
				}
			}
			 * </pre>
			 */
			WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getNewPhoneNoInfo(code);
			if (phoneNoInfo == null) {
				maMobile.setErrcode(-1);
				maMobile.setErrmsg("未获取到手机号码");
				return maMobile;
			}
			maMobile.setErrcode(0);
			maMobile.setPhoneNumber(phoneNoInfo.getPhoneNumber());
		} catch (WxErrorException e) {
			log.error("getMobileInfo error", e);
			WxError error = e.getError();
			maMobile.setErrcode(error.getErrorCode());
			maMobile.setErrmsg(error.getErrorMsg());
		}
		return maMobile;
	}
}
