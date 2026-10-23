package com.company.adminapi.controller;

/**
 * 通用上传下载（兼容若依前端组件）
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.adminapi.feign.client.FileFeign;
import com.company.token.accesscontrol.annotation.RequireLogin;
import com.company.framework.globalresponse.ExceptionUtil;
import com.company.tool.api.request.PresignedUploadReq;
import com.company.tool.api.response.PresignedUploadResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feiniaojin.gracefulresponse.api.ExcludeFromGracefulResponse;

import cn.hutool.http.HttpConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用上传下载（兼容若依前端组件）
 * <p>
 * 若依的 ImageUpload/FileUpload/Editor 组件固定调用 /common/upload 并直接读取
 * {code:200,fileName,url}，这里按该契约返回原始 JSON，不走 graceful-response 包装。
 */
@RequireLogin
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

	// 若依组件约定的成功码
	private static final String SUCCESS_CODE = "200";

	@Autowired
	private FileFeign fileFeign;
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 文件上传（兼容若依 /common/upload）
	 */
	@ExcludeFromGracefulResponse
	@PostMapping("/upload")
	public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
		if (file == null || file.getSize() == 0) {
			writeJson(response, StringUtils.EMPTY, "请选择文件");
			return;
		}
		String fileKey = uploadToFileServer(file);
		// 若依组件用 baseUrl + url 渲染，因此返回站内相对下载地址
		String downloadUrl = buildDownloadUrl(fileKey);
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("code", SUCCESS_CODE);
		result.put("fileName", downloadUrl);
		result.put("url", downloadUrl);
		result.put("fileKey", fileKey);
		result.put("originalFilename", file.getOriginalFilename());
		writeJson(response, result, null);
	}

	/**
	 * 文件上传（兼容若依 /file/upload 返回 fileKey）
	 */
	private String uploadToFileServer(MultipartFile file) {
		PresignedUploadResp presignedUploadResp;
		try {
			presignedUploadResp = fileFeign.presignedUpload(buildPresignedUploadReq(file.getOriginalFilename()));
		} catch (Exception e) {
			log.error("获取预签名上传地址失败", e);
			ExceptionUtil.throwException("文件上传失败");
			return null;
		}
		String fileKey = presignedUploadResp.getFileKey();
		String presignedUrl = presignedUploadResp.getPresignedUrl();

		// 使用预签名地址直传文件，避免文件流在微服务间传递
		HttpRequest httpRequest = HttpRequest.put(presignedUrl)
				.setConfig(HttpConfig.create().setUseDefaultContentTypeIfNull(false));
		try {
			httpRequest.body(file.getBytes());
		} catch (IOException e) {
			log.error("读取上传文件失败", e);
			ExceptionUtil.throwException("文件上传失败");
			return null;
		}
		try (HttpResponse httpResponse = httpRequest.execute()) {
			log.info("文件上传结果:{}", httpResponse.body());
		}
		return fileKey;
	}

	/**
	 * 构建预签名上传请求
	 */
	private PresignedUploadReq buildPresignedUploadReq(String fileName) {
		PresignedUploadReq req = new PresignedUploadReq();
		req.setBasePath("adminapi");
		req.setFileName(fileName);
		return req;
	}

	/**
	 * 构建站内下载地址
	 */
	private String buildDownloadUrl(String fileKey) {
		return "/file/download?fileKey=" + fileKey;
	}

	/**
	 * 下载文件（兼容若依 /common/download，按 fileKey 换取预签名地址后重定向）
	 */
	@ExcludeFromGracefulResponse
	@GetMapping("/download")
	public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
		Map<String, String> urlResult = fileFeign.presignedUrl(fileName);
		String url = urlResult == null ? null : urlResult.get("value");
		if (StringUtils.isBlank(url)) {
			writeJson(response, StringUtils.EMPTY, "文件不存在");
			return;
		}
		response.sendRedirect(url);
	}

	/**
	 * 输出 JSON
	 */
	private void writeJson(HttpServletResponse response, Object data, String msg) throws IOException {
		Map<String, Object> body = new LinkedHashMap<>();
		if (data instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) data;
			body.putAll(map);
		} else {
			body.put("code", SUCCESS_CODE);
			body.put("fileName", data);
		}
		if (msg != null) {
			body.put("code", "1");
			body.put("msg", msg);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		try (OutputStream outputStream = response.getOutputStream()) {
			outputStream.write(objectMapper.writeValueAsBytes(body));
		}
	}
}
