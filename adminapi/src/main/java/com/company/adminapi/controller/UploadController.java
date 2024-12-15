package com.company.adminapi.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.adminapi.filestorage.UploadService;
import com.company.common.api.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 上传
 * 
 * @author JQ棣
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	private UploadService uploadService;

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @return
	 */
	@PostMapping("/file")
	public Result<String> file(@RequestParam("file") MultipartFile file) {
		String name = file.getName();
		String originalFilename = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		log.info("name:{},originalFilename:{},contentType:{},size:{}", name, originalFilename, contentType, size);

		if (size == 0) {
			return Result.fail("请选择文件");
		}

		try (InputStream inputStream = file.getInputStream()) {
			String imgUrl = uploadService.upload(inputStream, originalFilename);
			return Result.success(imgUrl);
		} catch (IOException e) {
			log.error("IOException", e);
			return Result.fail("文件上传失败");
		}
	}
}
