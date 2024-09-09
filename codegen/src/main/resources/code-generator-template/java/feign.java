package com.company.{module}.api.feign;

import java.util.List;
import java.math.BigDecimal;

import com.company.common.request.RemoveReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.common.response.PageResp;
import com.company.{module}.api.constant.Constants;
import com.company.{module}.api.feign.fallback.{ModelName}FeignFallback;
import com.company.{module}.api.request.{ModelName}Req;
import com.company.{module}.api.response.{ModelName}Resp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/{modelName}", fallbackFactory = {ModelName}FeignFallback.class)
public interface {ModelName}Feign {

	@GetMapping("/page")
	Result<PageResp<{ModelName}Resp>> page({page_column_field_feign});
	
	@GetMapping("/list")
	Result<List<{ModelName}Resp>> list({column_field_feign});

	@GetMapping("/query")
	Result<{ModelName}Resp> query(@RequestParam("id") Integer id);

	@PostMapping("/save")
	Result<Boolean> save(@RequestBody {ModelName}Req {modelName}Req);

	@PostMapping("/update")
	Result<Boolean> update(@RequestBody {ModelName}Req {modelName}Req);

	@PostMapping("/remove")
	Result<Boolean> remove(@RequestBody RemoveReq<Integer> req);

}