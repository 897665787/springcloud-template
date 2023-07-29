package com.company.openapi.controller;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.sequence.SequenceGenerator;
import com.company.openapi.req.CreateOrderReq;
import com.google.common.collect.Maps;

/**
 * demo
 * 
 * @author JQ棣
 *
 */
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@GetMapping("/get")
	public Result<?> get(@NotEmpty(message = "订单号不能为空") String orderCode) {
		Map<String, String> result = Maps.newHashMap();
		result.put("orderCode", orderCode);
		result.put("productCode", "2222222");
		result.put("orderid", "" + sequenceGenerator.nextId());
		return Result.success(result);
	}

	@PostMapping("/create")
	public Result<?> create(@Valid @RequestBody CreateOrderReq createOrderReq) {
		Map<String, String> result = Maps.newHashMap();
		result.put("orderCode", createOrderReq.getOrderCode());
		result.put("productCode", createOrderReq.getProductCode());
		result.put("orderid", "" + sequenceGenerator.nextId());
		return Result.success(result);
	}
}
