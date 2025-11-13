package com.company.tool.api.feign;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.ThrowExceptionFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/webhook", fallbackFactory = ThrowExceptionFallback.class)
public interface WebhookFeign {

	@GetMapping("/select4PreTimeSend")
	Result<List<Integer>> select4PreTimeSend(@RequestParam("limit") Integer limit);

	@GetMapping("/exePreTimeSend")
	Result<Void> exePreTimeSend(@RequestParam("id") Integer id);
}
