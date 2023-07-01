package com.company.tool.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.common.api.Result;
import com.company.tool.api.request.UploadReq;
import com.company.tool.api.response.UploadResp;
import com.company.tool.enums.SmsEnum;
import com.company.tool.sms.AsyncSmsSender;
import com.google.common.collect.Lists;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/test")
public class TestController {

	@Autowired
	private FileController fileController;
	@Autowired
	private VerifyCodeController verifyCodeController;
	@Autowired
	private AsyncSmsSender asyncSmsSender;

	@GetMapping("/verifyCodeSms")
	public Result<String> verifyCodeSms(String mobile, String type) {
		return verifyCodeController.sms(mobile, type);
	}

	@GetMapping("/batchSendSms")
	public Result<List<Integer>> testBatchSendSms() {
		List<String> mobileList = Lists.newArrayList();
		mobileList.add("152xxxxxxxx");
		mobileList.add("153xxxxxxxx");
		mobileList.add("154xxxxxxxx");

		asyncSmsSender.send0(mobileList, SmsEnum.Type.MARKET);

		return Result.success();
	}
	
	@PostMapping("/upload")
	public Result<UploadResp> upload(@RequestParam("file") MultipartFile file) {
		String name = file.getName();
		String originalFilename = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		log.info("name:{},originalFilename:{},contentType:{},size:{}", name, originalFilename, contentType, size);

		if (size == 0) {
			return Result.fail("请选择文件");
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			byte[] bytes = IoUtil.readBytes(inputStream);
			UploadReq uploadReq = new UploadReq();
			uploadReq.setBytes(bytes);
			
			// 生成文件名
			uploadReq.setGeneratefileName(true);
			uploadReq.setFileName(originalFilename);
			
			
			/*
			// 生成文件名，带基础目录
			uploadReq.setGeneratefileName(true);
			uploadReq.setBasePath("images");
			uploadReq.setFileName(originalFilename);
			*/
			
			/*
			// 不生成文件名，不带基础目录，基础目录写到FileName
			uploadReq.setGeneratefileName(false);
			uploadReq.setFileName("images/" + originalFilename);
			*/
			
			/*
			// 不生成文件名
			uploadReq.setGeneratefileName(false);
			uploadReq.setBasePath("aaa");
			uploadReq.setFileName("images/" + originalFilename);
			*/
			
			return fileController.upload(uploadReq);
		} catch (IOException e) {
			log.error("IOException", e);
			return Result.fail(e.getMessage());
		}
	}
}
