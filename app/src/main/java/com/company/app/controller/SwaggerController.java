package com.company.app.controller;

import com.company.app.req.SwaggerReq;
import com.company.app.resp.SwaggerResp;
import com.company.common.api.Result;
import com.company.framework.util.PropertyUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/swagger")
public class SwaggerController {

	@PostMapping(value = "/post-body-row-result")
	public Result<Map<String, Object>> postbodyrowresult(@RequestBody Map<String, Object> param) {
		return Result.success(param);
	}

	@PostMapping(value = "/post-body-row2")
	public String postbodyrownoresult2(@RequestBody Map<String, Object> param) {
		return "SUCCESS";
	}

	@PostMapping(value = "/postparam")
	public Result<SwaggerResp> postparam(@RequestBody SwaggerReq param) {
		SwaggerResp paramResp = PropertyUtils.copyProperties(param, SwaggerResp.class);
		return Result.success(paramResp);
	}

	@GetMapping(value = "/getparam")
	public Result<SwaggerResp> getparam(SwaggerReq param) {
		SwaggerResp paramResp = PropertyUtils.copyProperties(param, SwaggerResp.class);
		return Result.success(paramResp);
	}
}
