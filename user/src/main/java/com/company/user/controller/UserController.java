package com.company.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;

@RestController
public class UserController implements UserFeign {

	@Override
	public UserResp getById(Long id) {
		return new UserResp().setOrderCode(System.currentTimeMillis() + "");
	}
}
