package com.company.adminapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.annotation.RequireLogin;

@RestController
@RequestMapping("/account")
public class AccountController {
	
    /**
     * 登录
     */
	@PostMapping(value = "/login")
	public Result<Void> login() {
		return Result.success();
	}
	
	/**
	 * 登出
	 * 
	 * @return 用户信息
	 */
	@RequireLogin
	@PostMapping(value = "/logout")
	public Result<Void> logout() {
		return Result.success();
	}
}
