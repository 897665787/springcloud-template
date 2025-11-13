package com.company.tool.api.feign;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.ThrowExceptionFallback;
import com.company.tool.api.request.NavReq;
import com.company.tool.api.response.NavResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/nav", fallbackFactory = ThrowExceptionFallback.class)
public interface NavFeign {

	@RequestMapping("/list")
	Result<List<NavResp>> list(@RequestBody NavReq navReq);
}
