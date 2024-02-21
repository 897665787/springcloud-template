package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.MemberBuyFeignFallback;
import com.company.user.api.request.MemberBuyOrderReq;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/memberBuy", fallbackFactory = MemberBuyFeignFallback.class)
public interface MemberBuyFeign {

	/**
	 * 购买
	 * 
	 * @param memberBuyOrderReq
	 * @return
	 */
	@PostMapping("/buy")
	Result<?> buy(@RequestBody MemberBuyOrderReq memberBuyOrderReq);

}
