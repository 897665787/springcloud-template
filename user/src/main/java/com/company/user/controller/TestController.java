package com.company.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.user.entity.UserInfo;
import com.company.user.service.UserInfoService;

@RestController
@RequestMapping("/test")
public class TestController{

	@Autowired
	private UserInfoService userInfoService;
	
	@GetMapping(value = "/getById")
	public Result<UserInfo> getById(Integer id) {
		UserInfo userInfo = userInfoService.selectById(id);
		return Result.success(userInfo);
	}
}
