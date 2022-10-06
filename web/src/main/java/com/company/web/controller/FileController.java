package com.company.web.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;
import com.company.web.file.FileStorage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PublicUrl
@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileStorage fileStorage;

	@PostMapping("/upload")
	public Result<String> upload(@RequestParam("file") MultipartFile file) {
		String name = file.getName();
		String originalFilename = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		log.info("name:{},originalFilename:{},contentType:{},size:{}", name, originalFilename, contentType, size);

		try (InputStream inputStream = file.getInputStream()) {
			String path = fileStorage.upload(inputStream, "images/" + originalFilename);
			return Result.success(path);
		} catch (IOException e) {
			log.error("IOException", e);
			return Result.fail(e.getMessage());
		}
	}
}
