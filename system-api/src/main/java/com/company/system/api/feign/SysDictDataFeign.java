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
import com.company.system.api.feign.fallback.SysDictDataFeignFallback;
import com.company.system.api.request.SysDictDataReq;
import com.company.system.api.response.SysDictDataResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/sysDictData", fallbackFactory = SysDictDataFeignFallback.class)
public interface SysDictDataFeign {

	@GetMapping("/page")
	Result<PageResp<SysDictDataResp>> page(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Long size, @RequestParam(value = "dictType", required = false) String dictType, @RequestParam(value = "dictCode", required = false) String dictCode, @RequestParam(value = "dictValue", required = false) String dictValue, @RequestParam(value = "dictSort", required = false) Integer dictSort, @RequestParam(value = "isDefault", required = false) String isDefault, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "dictRemark", required = false) String dictRemark);
	
	@GetMapping("/list")
	Result<List<SysDictDataResp>> list(@RequestParam(value = "dictType", required = false) String dictType, @RequestParam(value = "dictCode", required = false) String dictCode, @RequestParam(value = "dictValue", required = false) String dictValue, @RequestParam(value = "dictSort", required = false) Integer dictSort, @RequestParam(value = "isDefault", required = false) String isDefault, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "dictRemark", required = false) String dictRemark);

	@GetMapping("/query")
	Result<SysDictDataResp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody SysDictDataReq sysDictDataReq);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody SysDictDataReq sysDictDataReq);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

	@GetMapping("/getByType")
	Result<List<SysDictDataResp>> getByType(@RequestParam("type") String type);

	@GetMapping("/getValueByTypeCode")
	Result<String> getValueByTypeCode(@RequestParam("type") String type, @RequestParam("code") String code);
}