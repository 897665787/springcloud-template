package com.company.user.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.framework.context.HttpContextUtil;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;

@RestController
@RequestMapping("/user")
public class UserController implements UserFeign {

	@Autowired
	private OrderFeign orderFeign;
	
	@Override
	public UserResp getById(Long id) {
		System.out.println("UserController thread:"+Thread.currentThread());
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
		return new UserResp().setOrderCode(System.currentTimeMillis() + " " + "id:" + id);
	}
}
