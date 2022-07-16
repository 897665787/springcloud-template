package com.company.auth.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.context.HttpContextUtil;

@RestController
@RequestMapping("/change")
public class ChangeController {

	/**
	 * 授权头像、昵称
	 * 
	 * @return
	 */
	@GetMapping(value = "/nicknameheadicon")
	public Result<?> nicknameheadicon(String encryptedData, String iv, String wxcode) {
//		https://developers.weixin.qq.com/miniprogram/dev/api/open-api/login/wx.checkSession.html
		// 前端调用wx.checkSession检查是否需要重新获取wxcode
		String sessionKey = "";
		if (StringUtils.isBlank(wxcode)) {
			// 获取当前登录用户sessionKey
			String currentUserId = HttpContextUtil.currentUserId();
			sessionKey = "";
		} else {
			// 重新获取微信sessionKey
			sessionKey = "";
			// 更新当前用户sessionKey
		}
		try{
//			phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionkey, dto.getEncryptedData(), dto.getIv());
			
		} catch (Exception e) {
			// 解密异常，让前端重新获取wxcode，重新请求
		}
		
		return Result.success();
	}
	
	/**
	 * 修改密码
	 * 
	 * @param oldpassword
	 * @param newpassword
	 * @return
	 */
	@GetMapping(value = "/password")
	public Result<?> password(String oldpassword, String newpassword) {
		return Result.success();
	}
	
	@GetMapping(value = "/password2")
	public Result<?> password2(String code, String newpassword) {
		return Result.success();
	}
	
	/**
	 * 修改手机号
	 * 
	 * @param mobile
	 * @param code
	 * @return
	 */
	@GetMapping(value = "/checkmobile")
	public Result<?> checkmobile(String oldmobile, String code) {
		return Result.success();
	}
	
	@GetMapping(value = "/bindmobile")
	public Result<?> bindmobile(String newmobile, String code) {
		return Result.success();
	}

	/**
	 * 绑定微信
	 * 
	 * @param wxcode
	 * @return
	 */
	@GetMapping(value = "/bindWx")
	public Result<?> bindWx(String wxcode) {
		return Result.success();
	}
	
	/**
	 * 解绑微信
	 * 
	 * @return
	 */
	@GetMapping(value = "/unbindWx")
	public Result<?> unbindWx() {
		return Result.success();
	}
	
	/**
	 * 注销账户
	 * 
	 * @return
	 */
	@GetMapping(value = "/cancelAccount")
	public Result<?> cancelAccount() {
		return Result.success();
	}
}
