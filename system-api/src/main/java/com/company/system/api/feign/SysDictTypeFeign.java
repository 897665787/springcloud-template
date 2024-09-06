package com.company.system.api.feign;

import java.util.List;

import com.company.common.request.RemoveReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.common.response.PageResp;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.SysDictTypeFeignFallback;
import com.company.system.api.request.SysDictTypeReq;
import com.company.system.api.response.SysDictTypeResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysDictType", fallbackFactory = SysDictTypeFeignFallback.class)
public interface SysDictTypeFeign {

	@GetMapping("/page")
	Result<PageResp<SysDictTypeResp>> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "dictName", required = false) String dictName, @RequestParam(value = "dictType", required = false) String dictType, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "dictRemark", required = false) String dictRemark);
	
	@GetMapping("/list")
	Result<List<SysDictTypeResp>> list(@RequestParam(value = "dictName", required = false) String dictName, @RequestParam(value = "dictType", required = false) String dictType, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "dictRemark", required = false) String dictRemark);

	@GetMapping("/query")
	Result<SysDictTypeResp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody SysDictTypeReq sysDictTypeReq);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody SysDictTypeReq sysDictTypeReq);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

}