package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.RechargeOrderFeignFallback;
import com.company.user.api.request.RechargeOrderReq;
import com.company.user.api.response.RechargeOrderResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/rechargeOrder", fallbackFactory = RechargeOrderFeignFallback.class)
public interface RechargeOrderFeign {

	/**
	 * 购买
	 *
	 * @param rechargeOrderReq
	 * @return
	 */
	@PostMapping("/buy")
	RechargeOrderResp buy(@RequestBody RechargeOrderReq rechargeOrderReq);

}
