package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.DistributeOrderFeignFallback;
import com.company.user.api.request.DistributeOrderReq;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/distributeOrder", fallbackFactory = DistributeOrderFeignFallback.class)
public interface DistributeOrderFeign {

	/**
	 * 购买
	 * 
	 * @param distributeOrderReq
	 * @return
	 */
	@PostMapping("/buy")
	Result<?> buy(@RequestBody DistributeOrderReq distributeOrderReq);

}
