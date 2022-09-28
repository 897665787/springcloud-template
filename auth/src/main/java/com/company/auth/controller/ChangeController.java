package com.company.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;

@RestController
@RequestMapping("/change")
public class ChangeController {

	/**
	 * 小程序授权头像、昵称（非敏感信息，直接从rawData获取即可）
	 * 
	 * @return
	 */
	@GetMapping(value = "/nicknameheadicon")
	public Result<?> nicknameheadicon(String nickName, String avatarUrl) {
//		https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/signature.html#%E4%BC%9A%E8%AF%9D%E5%AF%86%E9%92%A5-session-key-%E6%9C%89%E6%95%88%E6%80%A7
		
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
