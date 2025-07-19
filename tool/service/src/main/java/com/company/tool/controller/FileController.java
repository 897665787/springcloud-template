package com.company.tool.controller;

import com.company.common.api.Result;
import com.company.tool.api.feign.FileFeign;
import com.company.tool.api.request.ClientUploadReq;
import com.company.tool.api.request.UploadReq;
import com.company.tool.api.response.ClientUploadResp;
import com.company.tool.api.response.UploadResp;
import com.company.tool.filestorage.ClientUploadResult;
import com.company.tool.filestorage.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/file")
public class FileController implements FileFeign {

	@Autowired
	private UploadService uploadService;

	@Override
	public Result<UploadResp> upload(@RequestBody UploadReq uploadReq) {
		if (uploadReq.getBytes().length == 0) {
			return Result.fail("请选择文件");
		}

		String fileKey = uploadService.upload(uploadReq.getBytes(), uploadReq.getBasePath(), uploadReq.getFileName());

		UploadResp resp = new UploadResp();
		resp.setFileKey(fileKey);
		return Result.success(resp);
	}

	@Override
	public Result<ClientUploadResp> clientUpload(@RequestBody ClientUploadReq clientUploadReq) {
		String basePath = clientUploadReq.getBasePath();
		String fileName = clientUploadReq.getFileName();
		ClientUploadResult clientUploadResult = uploadService.clientUpload(basePath, fileName);
		ClientUploadResp resp = new ClientUploadResp();
		resp.setFileKey(clientUploadResult.getFileKey());
		resp.setPresignedUrl(clientUploadResult.getPresignedUrl());
		return Result.success(resp);
	}

	@Override
	public Result<String> presignedUrl(String fileKey) {
		String presignedUrl = uploadService.presignedUrl(fileKey);
		return Result.success(presignedUrl);
	}

}
