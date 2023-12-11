package com.company.auth.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.auth.authentication.test.TestData;
import com.company.common.api.Result;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping(value = "/data")
	public Result<Map<String, Object>> testdata() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userInfoList", TestData.userInfoList);
		params.put("oauthList", TestData.oauthList);
		params.put("redis", TestData.redis);
		return Result.success(params);
	}
}
