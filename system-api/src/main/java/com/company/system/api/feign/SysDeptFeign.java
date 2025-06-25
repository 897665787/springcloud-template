package com.company.system.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.constant.Constants;
import com.company.system.api.feign.fallback.SysDeptFeignFallback;
import com.company.system.api.request.SysDeptReq;
import com.company.system.api.response.SysDeptResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysDept", fallbackFactory = SysDeptFeignFallback.class)
public interface SysDeptFeign {

	@GetMapping("/page")
	PageResp<SysDeptResp> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "parentId", required = false) Integer parentId, @RequestParam(value = "parentIds", required = false) String parentIds, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "orderNum", required = false) Integer orderNum, @RequestParam(value = "status", required = false) String status);
	
	@GetMapping("/list")
	List<SysDeptResp> list(@RequestParam(value = "parentId", required = false) Integer parentId, @RequestParam(value = "parentIds", required = false) String parentIds, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "orderNum", required = false) Integer orderNum, @RequestParam(value = "status", required = false) String status);

	@GetMapping("/query")
	SysDeptResp query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Boolean save(@RequestBody SysDeptReq sysDeptReq);

	@PostMapping("/update")
	Boolean update(@RequestBody SysDeptReq sysDeptReq);

	@PostMapping("/remove")
	Boolean remove(@RequestBody RemoveReq<Integer> req);

}
