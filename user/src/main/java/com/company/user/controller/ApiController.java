package com.company.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.order.api.feign.OrderFeign;
import com.company.order.api.response.OrderResp;
import com.company.user.entity.User;
import com.company.user.service.UserService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

	@Autowired
	private UserService userService;
	@Autowired
	private OrderFeign orderFeign;

	@Value("${server.port}")
	private String port;

	@GetMapping(value = "/onoff")
	public OrderResp onoff() {
		for (int i = 0; i < 100; i++) {
			OrderResp byId = orderFeign.getById((long)i);
			log.info("onoff:{}",byId);
		}
		return new OrderResp();
	}
	
	@GetMapping(value = "/info")
	public String info() {
		return "{}";
	}

	@GetMapping(value = "/getById")
	public User getById(Long id) {
		log.info("provider-6001");
//		User byId = userService.getById(id);
		OrderResp byId = orderFeign.getById(id);
		System.out.println("byId:"+byId);
//		userService.saveTs();
		return new User();
	}

	@GetMapping(value = "/list")
	public List<User> list() {
		List<User> list = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(new User().setId(Long.valueOf(i)).setUsername(String.valueOf(System.currentTimeMillis())));
		}
		return list;
	}
	
	@PostMapping(value = "/save")
	public User save() {
		log.info("provider-6001");
		userService.save();
		return new User();
	}
}
