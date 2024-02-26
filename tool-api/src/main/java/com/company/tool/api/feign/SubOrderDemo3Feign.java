package com.company.tool.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.SubOrderDemo3FeignFallback;
import com.company.tool.api.request.SubOrderDemo3OrderReq;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/subOrderDemo3", fallbackFactory = SubOrderDemo3FeignFallback.class)
public interface SubOrderDemo3Feign {

	/**
	 * 购买
	 * 
	 * @param subOrderDemo3OrderReq
	 * @return
	 */
	@PostMapping("/buy")
	Result<?> buy(@RequestBody SubOrderDemo3OrderReq subOrderDemo3OrderReq);

}
