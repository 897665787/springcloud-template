package com.company.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.company.user.api.feign.OpenAccessAccountFeign;
import com.company.user.service.OpenAccessAccountService;

@RestController
@RequestMapping("/openAccessAccount")
public class OpenAccessAccountController implements OpenAccessAccountFeign {

	@Autowired
	private OpenAccessAccountService openAccessAccountService;

	@Override
	public String getAppKeyByAppid(String appid) {
		String appKey = openAccessAccountService.getAppKeyByAppid(appid);
		return appKey;
	}
}
