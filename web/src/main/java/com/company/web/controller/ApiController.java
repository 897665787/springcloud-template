package com.company.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.exception.BusinessException;
import com.company.framework.deploy.RefreshHandler;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.response.OrderResp;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

	@Autowired
	private OrderFeign orderFeign;
	@Autowired
	private RefreshHandler refreshHandler;

	@GetMapping(value = "/info")
	public String info() {
		return "{}";
	}
	
	@GetMapping(value = "/getOrderById")
	public OrderResp getOrderById(Long id) {
		if (true)
			throw new BusinessException(1, "aaaaaaaaaaa");
		OrderResp byId = orderFeign.getById(id);
		return byId;
	}
	
	@GetMapping(value = "/getInt")
	public Integer getInt() {
		return 1;
	}
	
	@GetMapping(value = "/getString")
	public String getString() {
		return "addddddd";
	}
	
	@GetMapping(value = "/time")
	public Date time() {
		return new Date();
	}
	
	@GetMapping(value = "/onoff")
	public OrderResp onoff() {
		for (int i = 0; i < 10000; i++) {
			OrderResp byId = orderFeign.getById((long)i);
			log.info("onoff:{}",byId);
		}
		return new OrderResp();
	}

	@GetMapping(value = "/send")
	public String send() {
		refreshHandler.notify2Refresh("test");
		return "{}";
	}

	
}
