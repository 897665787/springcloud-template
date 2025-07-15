package com.company.system.api.feign;

import java.util.List;

import com.company.system.api.request.RemoveReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.system.api.response.PageResp;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.SysConfigFeignFallback;
import com.company.system.api.request.SysConfigReq;
import com.company.system.api.response.SysConfigResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysConfig", fallbackFactory = SysConfigFeignFallback.class)
public interface SysConfigFeign {

	@GetMapping("/page")
	Result<PageResp<SysConfigResp>> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "value", required = false) String value, @RequestParam(value = "configRemark", required = false) String configRemark);

	@GetMapping("/list")
	Result<List<SysConfigResp>> list(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "value", required = false) String value, @RequestParam(value = "configRemark", required = false) String configRemark);

	@GetMapping("/query")
	Result<SysConfigResp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody SysConfigReq sysConfigReq);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody SysConfigReq sysConfigReq);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

	@GetMapping("/getValueByCode")
	Result<String> getValueByCode(@RequestParam("code") String code);

	@PostMapping("/updateValueByCode")
	Result<Boolean> updateValueByCode(@RequestParam("value") String value, @RequestParam("code") String code);

}
