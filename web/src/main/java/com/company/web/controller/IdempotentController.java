package com.company.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.util.PropertyUtils;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.request.UserReq;
import com.company.user.api.response.UserResp;

@RestController
@RequestMapping("/idempotent")
public class IdempotentController {

	@Autowired
	private UserFeign userFeign;
	
	@RequestMapping(value = "/createUser")
	public Result<com.company.web.resp.UserResp> createUser(@RequestBody @Valid UserReq userReq) {
		UserResp userResp = userFeign.idempotent(userReq).dataOrThrow();
		com.company.web.resp.UserResp resp = PropertyUtils.copyProperties(userResp, com.company.web.resp.UserResp.class);
		return Result.success(resp);
//		userFeign.noreturn();
//		return Result.success(new UserResp());
	}
}
