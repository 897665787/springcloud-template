package com.company.order.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.feign.fallback.OrderFeignFallback;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;

@FeignClient(value = "template-order", path = "/order", fallbackFactory = OrderFeignFallback.class)
public interface OrderFeign {

	@RequestMapping("/getById")
	Result<OrderResp> getById(@RequestParam("id") Long id);

	@RequestMapping("/save")
	Result<OrderResp> save(@RequestBody OrderReq orderReq);

	@RequestMapping("/retryGet")
	Result<OrderResp> retryGet(@RequestParam("id") Long id);
	
	@PostMapping("/retryPost")
	Result<OrderResp> retryPost(@RequestBody OrderReq orderReq);
}
