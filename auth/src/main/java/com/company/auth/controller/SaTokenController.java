package com.company.auth.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/sa-token")
public class SaTokenController {
	
	@PublicUrl
	@PostMapping(value = "/cookie/login")
	public Result<Object> login(@RequestBody Map<String, Object> param) {
		// 手机号+验证码
		// 手机号+密码
		// 小程序微信授权(根据code找到openid或unionid)
//		WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
		// APP微信授权(根据code找到openid或unionid)
		
		String userId = "83848";
		StpUtil.login(userId);
//		StpUtil.getTokenSession().set(key, value);
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		return Result.success(tokenInfo);
	}
	
	@GetMapping(value = "/cookie/check")
	public Result<String> check() {
		
		String msg = "状态正常";
		try {
			// 获取当前会话账号id, 如果未登录，则抛出异常：`NotLoginException`
			int loginId = StpUtil.getLoginIdAsInt();
			System.out.println("loginId:"+loginId);
			System.out.println("getLoginDevice:"+StpUtil.getLoginDevice());
//			System.out.println("getLoginKey:"+StpUtil.getLoginKey());
			System.out.println("getTokenValue:"+StpUtil.getTokenValue());
			System.out.println("getLoginIdByToken:"+StpUtil.getLoginIdByToken(StpUtil.getTokenValue()));
			System.out.println("getSession:"+JSONUtil.toJsonPrettyStr(StpUtil.getSession()));
			System.out.println("getSession getId:"+StpUtil.getSession().getId());
			System.out.println("getTokenInfo:"+JSONUtil.toJsonPrettyStr(StpUtil.getTokenInfo()));
			System.out.println("getTokenSession:"+JSONUtil.toJsonPrettyStr(StpUtil.getTokenSession()));
			System.out.println("isLogin:"+StpUtil.isLogin());
			StpUtil.checkLogin();
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return Result.success(msg);
	}
	
	@PostMapping(value = "/cookie/logout")
	public Result<String> logout(@RequestBody Map<String, Object> param) {
		StpUtil.logout();
		return Result.success("注销成功");
	}
	
	@PublicUrl
	@PostMapping(value = "/jwt/login")
	public Result<String> jwtlogin(@RequestBody Map<String, Object> param) {
		// 手机号+验证码
		// 手机号+密码
		// 小程序微信授权(根据code找到openid或unionid)
//		WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
		// APP微信授权(根据code找到openid或unionid)
		
		String userId = "83848";
		StpUtil.login(userId);
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		System.out.println("getTokenName():"+tokenInfo.getTokenName());
		System.out.println("getTokenValue():"+tokenInfo.getTokenValue());
		return Result.success(tokenInfo.getTokenValue());
	}
	
	@GetMapping(value = "/jwt/check")
	public Result<String> jwtcheck() {
		
		String msg = "状态正常";
		try {
			// 获取当前会话账号id, 如果未登录，则抛出异常：`NotLoginException`
			int loginId = StpUtil.getLoginIdAsInt();
			System.out.println("loginId:"+loginId);
			System.out.println("getLoginDevice:"+StpUtil.getLoginDevice());
//			System.out.println("getLoginKey:"+StpUtil.getLoginKey());
			System.out.println("getTokenValue:"+StpUtil.getTokenValue());
			System.out.println("getLoginIdByToken:"+StpUtil.getLoginIdByToken(StpUtil.getTokenValue()));
//			System.out.println("getSession:"+JSONUtil.toJsonPrettyStr(StpUtil.getSession()));
//			System.out.println("getSession getId:"+StpUtil.getSession().getId());
			System.out.println("getTokenInfo:"+JSONUtil.toJsonPrettyStr(StpUtil.getTokenInfo()));
//			System.out.println("getTokenSession:"+JSONUtil.toJsonPrettyStr(StpUtil.getTokenSession()));
			StpUtil.checkLogin();
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return Result.success(msg);
	}
	
	@PostMapping(value = "/jwt/logout")
	public Result<String> jwtlogout(@RequestBody Map<String, Object> param) {
		StpUtil.logout();
		return Result.success("注销成功");
	}
}
