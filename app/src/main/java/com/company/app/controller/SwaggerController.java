package com.company.app.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.app.req.SwaggerReq;
import com.company.app.resp.SwaggerResp;
import com.company.common.api.Result;
import com.company.common.util.PropertyUtils;
import com.company.framework.annotation.EncryptResultData;

@RestController
@RequestMapping("/swagger")
public class SwaggerController {

	@PostMapping(value = "/post-body-row-result")
	@EncryptResultData
	public Result<Map<String, Object>> postbodyrowresult(@RequestBody Map<String, Object> param) {
		return Result.success(param);
	}

	@PostMapping(value = "/post-body-row2")
	@EncryptResultData
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
