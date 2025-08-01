package com.company.user.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.util.JsonUtil;
import com.company.framework.util.PropertyUtils;
import com.company.order.api.feign.OrderFeign;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.request.UserReq;
import com.company.user.api.response.UserResp;
import com.company.user.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController implements UserFeign {

	@Autowired
	private OrderFeign orderFeign;

	@Override
	public Result<UserResp> getById(Long id) {
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
		User user = new User();
		user.setId(1L).setName("adasd").setStatus(2);
		System.out.println(JsonUtil.toJsonString(user));
		return Result.success(PropertyUtils.copyProperties(user, UserResp.class));
	}

	@Override
	public Result<UserResp> retryGet(Long id) {
		log.info("retryGet");
		User user = new User();
		user.setId(1L).setName("retryGet1").setStatus(2);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		log.info("retryGet:{}", user);
		return Result.success(PropertyUtils.copyProperties(user, UserResp.class));
	}

	@Override
	public Result<UserResp> retryPost(@RequestBody UserReq userReq) {
		log.info("retryGet");
		User user = new User();
		user.setId(1L).setName("adasd").setStatus(2);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		log.info("retryGet:{}", user);
		return Result.success(PropertyUtils.copyProperties(user, UserResp.class));
	}

	@Override
	public Result<Void> noreturn() {
		String value = System.currentTimeMillis() + "";
		System.out.println(" value:" + value);
		try {
			Thread.sleep(new Random().nextInt(2) == 0 ? 500 : 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Result.success();
	}
}
