package com.company.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.EncryptResultData;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;

@RestController
@RequestMapping("/encrypt")
public class EncryptController {

	@Autowired
	private UserFeign userFeign;
	
	@PostMapping(value = "/post-body-row")
	@EncryptResultData
	public Result<UserResp> postbodyrow(@RequestBody Map<String, Object> param) {
//		if(true){
//			throw new BusinessException("asdasd");
//		}
		
		Result<UserResp> byId = userFeign.getById(1L);
		System.out.println("byId:"+JsonUtil.toJsonString(byId));
		return byId;
	}

	@PostMapping(value = "/post-body-row-result")
	@EncryptResultData
	public Result<Map<String, Object>> postbodyrowresult(@RequestBody Map<String, Object> param) {
		return Result.success(param);
	}

	@PostMapping(value = "/post-body-row2")
	@EncryptResultData
	public String postbodyrownoresult2(@RequestBody Map<String, Object> param) {
		return "SUCCESS";
	}
}
