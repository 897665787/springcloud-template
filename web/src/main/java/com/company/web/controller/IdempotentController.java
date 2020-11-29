package com.company.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.user.api.feign.UserFeign;
import com.company.user.api.request.UserReq;
import com.company.user.api.response.UserResp;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/idempotent")
@Slf4j
public class IdempotentController {

	@Autowired
	private UserFeign userFeign;
	
	@RequestMapping(value = "/createUser")
	public UserResp createUser(@RequestBody @Valid UserReq userReq) {
//		UserResp idempotent = userFeign.idempotent(userReq);
//		return idempotent;
		userFeign.noreturn();
		return new UserResp();
	}
}
