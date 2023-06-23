package com.company.tool.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.tool.api.feign.fallback.SmsFeignFallback;

@FeignClient(value = "template-tool", path = "/sms", fallbackFactory = SmsFeignFallback.class)
public interface SmsFeign {

	@GetMapping("/select4PreTimeSend")
	Result<List<Integer>> select4PreTimeSend(@RequestParam("limit") Integer limit);

	@GetMapping("/exePreTimeSend")
	Result<Void> exePreTimeSend(@RequestParam("id") Integer id);
}
