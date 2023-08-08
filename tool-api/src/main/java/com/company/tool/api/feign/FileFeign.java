package com.company.tool.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.company.common.api.Result;
import com.company.tool.api.feign.fallback.FileFeignFallback;
import com.company.tool.api.request.UploadReq;
import com.company.tool.api.response.UploadResp;

@FeignClient(value = "template-tool", path = "/file", fallbackFactory = FileFeignFallback.class)
public interface FileFeign {

	@PostMapping("/upload")
	Result<UploadResp> upload(@RequestBody UploadReq uploadReq);

	@PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	Result<UploadResp> uploadFile(@RequestPart("file") MultipartFile file);
}