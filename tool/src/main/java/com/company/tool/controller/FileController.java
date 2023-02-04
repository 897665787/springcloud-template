package com.company.tool.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.tool.api.feign.FileFeign;
import com.company.tool.api.request.UploadReq;
import com.company.tool.api.response.UploadResp;
import com.company.tool.file.FileStorage;
import com.company.tool.file.FileUrl;

import cn.hutool.core.util.IdUtil;

@RestController
@RequestMapping(value = "/file")
public class FileController implements FileFeign {

	@Autowired
	private FileStorage fileStorage;

	@Override
	public Result<UploadResp> upload(@RequestBody UploadReq uploadReq) {
		if (uploadReq.getBytes().length == 0) {
			return Result.fail("请选择文件");
		}
		
		String fileName = uploadReq.getFileName();
		if (uploadReq.isGeneratefileName()) {
			fileName = generateFileName(fileName);
		}
		String fullFileName = fullFileName(uploadReq.getBasePath(), fileName);

		InputStream inputStream = new ByteArrayInputStream(uploadReq.getBytes());
		FileUrl fileUrl = fileStorage.upload(inputStream, fullFileName);
		
		UploadResp resp = new UploadResp();
		resp.setDomainUrl(fileUrl.getDomainUrl());
		resp.setOssUrl(fileUrl.getOssUrl());
		return Result.success(resp);
	}

	private static String fullFileName(String basePath, String fileName) {
		if (basePath == null) {
			basePath = "";
		}
		if (basePath.startsWith("/")) {
			basePath = basePath.substring(1, basePath.length());
		}
		if (StringUtils.isNotBlank(basePath) && !basePath.endsWith("/")) {
			basePath = basePath + "/";
		}
		return basePath + fileName;
	}
	
	private static String generateFileName(String fileName) {
		// 生成文件名（目录+文件名，例如:basePath/202201/01/00033d9fea0b484eac1509567e87e61a.jpg）
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);

		String ext = null;
		String[] fileNameSplit = fileName.split("\\.");
		if (fileNameSplit.length > 1) {
			ext = fileNameSplit[fileNameSplit.length - 1];
		}
		return String.format("%d%02d/%02d/%s.%s", year, month, day, IdUtil.fastSimpleUUID(), ext);
	}
}
