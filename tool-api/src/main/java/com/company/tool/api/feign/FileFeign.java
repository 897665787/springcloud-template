package com.company.tool.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.tool.api.feign.fallback.FileFeignFallback;
import com.company.tool.api.request.UploadReq;

@FeignClient(value = "template-tool", path = "/file", fallbackFactory = FileFeignFallback.class)
public interface FileFeign {

	@PostMapping("/upload")
	Result<String> upload(@RequestBody UploadReq uploadReq);
}
