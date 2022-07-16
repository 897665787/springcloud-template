package com.company.auth.authentication.impl.tool.impl;

import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IMpTool;
import com.company.auth.authentication.impl.tool.dto.MpAccessToken;
import com.company.auth.authentication.impl.tool.dto.MpUserInfo;
import com.company.auth.wx.mp.config.WxMpConfiguration;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

@Slf4j
@Component
public class MpTool implements IMpTool {

//	private static final int HTTP_TIMEOUT = 5000;// http超时时长，-1表示默认超时，单位毫秒

	@Override
	public MpAccessToken getAccessToken(String appid, String code) {
		/**
		https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		{
			"access_token": "58_hutKyhcT5biJsn04NZ9jHW9GZVgCVOF4YVlK3NZiivKq8rJkWs2-la-nhJNhuisNs-fwFtfeaIyOuWNYC9AOW9fzv3oz_pMcg_zUYoZplZc",
			"expires_in": 7200,
			"refresh_token": "58_luzGuma7ZudDzx4kF1-YCo9wNUI8D9-FWDLCtGg6idqwBy7yJ2ssAMpjgNR-uQz603Mrm2gadX5B1XRHZ76gEMooN-T2Fn514skqOWTOWMM",
			"openid": "oYB7c6mHjNn4LW_zXXPsDR8mVSJ8",
			"scope": "snsapi_userinfo"
		}
		 */
		/*
		String secret = findByAppid(appid);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		url = url.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
		
		String result = HttpUtil.get(url, HTTP_TIMEOUT);
		log.info("access_token,url:{}, result:{}", url, result);
		
		MpAccessToken mpAccessToken = JsonUtil.toEntity(result, MpAccessToken.class);
		*/
		
		MpAccessToken mpAccessToken = new MpAccessToken();
		final WxMpService wxService = WxMpConfiguration.getMpService(appid);
        try {
            WxOAuth2AccessToken accessToken = wxService.getOAuth2Service().getAccessToken(code);
            mpAccessToken.setErrcode(0);
            mpAccessToken.setAccessToken(accessToken.getAccessToken());
            mpAccessToken.setOpenid(accessToken.getOpenId());
            mpAccessToken.setUnionid(accessToken.getUnionId());
        	
//            WxOAuth2UserInfo user = wxService.getOAuth2Service().getUserInfo(accessToken, "zh_CN");
//            map.put("user", user);
        } catch (WxErrorException e) {
			log.error("getAccessToken error", e);
            WxError error = e.getError();
            mpAccessToken.setErrcode(error.getErrorCode());
            mpAccessToken.setErrmsg(error.getErrorMsg());
        }
		return mpAccessToken;
	}
	
	@Override
	public MpUserInfo getUserinfo(String accessToken, String openid) {
		/**
		https://api.weixin.qq.com/sns/userinfo?access_token=58_hutKyhcT5biJsn04NZ9jHW9GZVgCVOF4YVlK3NZiivKq8rJkWs2-la-nhJNhuisNs-fwFtfeaIyOuWNYC9AOW9fzv3oz_pMcg_zUYoZplZc&openid=oYB7c6mHjNn4LW_zXXPsDR8mVSJ8
		{
			"openid": "oYB7c6mHjNn4LW_zXXPsDR8mVSJ8",
			"nickname": "勝",
			"sex": 0,
			"language": "",
			"city": "",
			"province": "",
			"country": "",
			"headimgurl": "https://thirdwx.qlogo.cn/mmopen/vi_32/4gV1O4eXicKuKlmuG5aniaNadRm3hkbbdp5qRd8MMY946ia1eDiah2ttfFeLRp8CEn5HFda6AKCM67GWbDj11dzy1Q/132",
			"privilege": [],
			"unionid": "oiPIJuEo0OzxLqzSEWZYZ-nVWmTU"
		}
		 */
		/*
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
		url = url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);

		String result = HttpUtil.get(url, HTTP_TIMEOUT);
		log.info("userinfo,url:{}, result:{}", url, result);
		
		MpUserInfo mpUserInfo = JsonUtil.toEntity(result, MpUserInfo.class);
		*/

		MpUserInfo mpUserInfo = new MpUserInfo();
		final WxMpService wxService = WxMpConfiguration.getMpService0();
		try {
			WxOAuth2AccessToken wxOAuth2AccessToken = new WxOAuth2AccessToken();
			wxOAuth2AccessToken.setAccessToken(accessToken);
			wxOAuth2AccessToken.setOpenId(openid);

			WxOAuth2UserInfo user = wxService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");

			mpUserInfo.setErrcode(0);
			mpUserInfo.setOpenid(user.getOpenid());
			mpUserInfo.setNickname(user.getNickname());
			mpUserInfo.setHeadimgurl(user.getHeadImgUrl());
			mpUserInfo.setUnionid(user.getUnionId());
		} catch (WxErrorException e) {
			log.error("getUserInfo error", e);
			WxError error = e.getError();
			mpUserInfo.setErrcode(error.getErrorCode());
			mpUserInfo.setErrmsg(error.getErrorMsg());
		}
		return mpUserInfo;
	}
}
