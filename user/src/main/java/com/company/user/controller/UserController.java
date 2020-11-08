package com.company.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.util.JsonUtil;
import com.company.common.util.PropertyUtils;
import com.company.order.api.feign.OrderFeign;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;
import com.company.user.entity.User;

@RestController
@RequestMapping("/user")
public class UserController implements UserFeign {

	@Autowired
	private OrderFeign orderFeign;
	
	@Override
	public UserResp getById(Long id) {
		System.out.println("UserController thread:"+Thread.currentThread());
		/*
		HttpServletRequest request = HttpContextUtil.request();
		System.out.println("request:" + request);
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			System.out.println("headerName:" + headerName + " headerValue:" + headerValue);
		}
		OrderResp save = orderFeign.save(new OrderReq().setId(System.currentTimeMillis()));
		System.out.println("UserController.save():"+save);
		System.out.println("currentUserId:" + HttpContextUtil.currentUserId());
		*/
		User user = User.builder().id(1L).name("adasd").status(2).build();
		System.out.println(JsonUtil.toJsonString(user));
		return PropertyUtils.copyProperties(user, UserResp.class);
	}
}
