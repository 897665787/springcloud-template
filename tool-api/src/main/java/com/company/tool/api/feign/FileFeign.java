package com.company.tool.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.FileFeignFallback;
import com.company.tool.api.request.UploadReq;
import com.company.tool.api.response.UploadResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/file", fallbackFactory = FileFeignFallback.class)
public interface FileFeign {

	@PostMapping("/upload")
	Result<UploadResp> upload(@RequestBody UploadReq uploadReq);
}
