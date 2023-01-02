package com.company.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.context.SpringContextUtil;
import com.company.user.entity.UserInfo;
import com.company.user.service.UserInfoService;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/test")
public class TestController{

	@Autowired
	private UserInfoService userInfoService;
	
	@GetMapping(value = "/beans")
	public Result<Map<?,?>> beans() {
		ApplicationContext context = SpringContextUtil.getContext();
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		Map<String, Object> map = Maps.newHashMap();
		map.put("beanDefinitionNames", beanDefinitionNames);
		return Result.success(map);
	}
	
	@GetMapping(value = "/getById")
	public Result<UserInfo> getById(Integer id) {
		UserInfo userInfo = userInfoService.selectById(id);
		return Result.success(userInfo);
	}
	
	@GetMapping(value = "/save")
	public Result<Void> save(Integer id) {
		UserInfo entity = new UserInfo();
		entity.setNickname("1111");
		userInfoService.insert(entity);
		return Result.success();
	}
}
