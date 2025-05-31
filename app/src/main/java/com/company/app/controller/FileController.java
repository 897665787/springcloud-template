package com.company.app.controller;

import cn.hutool.http.HttpRequest;
import com.company.common.api.Result;
import com.company.tool.api.feign.FileFeign;
import com.company.tool.api.request.ClientUploadReq;
import com.company.tool.api.response.ClientUploadResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileFeign fileFeign;

	@PostMapping("/upload")
	public Result<String> upload(@RequestParam("file") MultipartFile file) {
		String name = file.getName();
		String originalFilename = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		log.info("name:{},originalFilename:{},contentType:{},size:{}", name, originalFilename, contentType, size);

		if (size == 0) {
			return Result.fail("请选择文件");
		}

		ClientUploadReq clientUploadReq = new ClientUploadReq();
		clientUploadReq.setBasePath("adminapi");
		clientUploadReq.setFileName(originalFilename);
		ClientUploadResp clientUploadResp = fileFeign.clientUpload(clientUploadReq).dataOrThrow();
		String fileKey = clientUploadResp.getFileKey();
		String presignedUrl = clientUploadResp.getPresignedUrl();

		try (InputStream inputStream = file.getInputStream()) {
			// 客户端使用presignedUrl上传文件
			String result = HttpRequest.put(presignedUrl).body(IOUtils.toByteArray(inputStream)).execute().body();
			log.info("result:{}", result);
			return Result.success(fileKey);
		} catch (IOException e) {
			log.error("IOException", e);
			return Result.fail("文件上传失败");
		}
	}

	@PostMapping("/clientUpload")
	public Result<ClientUploadResp> clientUpload(String fileName) {
		ClientUploadReq clientUploadReq = new ClientUploadReq();
		clientUploadReq.setBasePath("web");
		clientUploadReq.setFileName(fileName);
		return fileFeign.clientUpload(clientUploadReq);
	}

	/**
     * 获取访问链接
     *
     * @param fileKey
     * @return
     */
	@GetMapping("/url")
	public Result<String> url(String fileKey) {
		return fileFeign.presignedUrl(fileKey);
	}
}