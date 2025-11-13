package com.company.tool.api.feign;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.ThrowExceptionFallback;
import com.company.tool.api.request.BannerReq;
import com.company.tool.api.response.BannerResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/banner", fallbackFactory = ThrowExceptionFallback.class)
public interface BannerFeign {

	@RequestMapping("/list")
	Result<List<BannerResp>> list(@RequestBody BannerReq bannerReq);
}
