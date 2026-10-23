package com.company.adminapi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import com.company.framework.globalresponse.ExceptionUtil;
import com.company.adminapi.feign.client.FileFeign;
import com.company.tool.api.request.PresignedUploadReq;
import com.company.tool.api.response.PresignedUploadResp;

import cn.hutool.http.HttpConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileFeign fileFeign;

	@PostMapping("/upload")
	public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
		String name = file.getName();
		String originalFilename = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		log.info("name:{},originalFilename:{},contentType:{},size:{}", name, originalFilename, contentType, size);

		if (size == 0) {
			ExceptionUtil.throwException("请选择文件");
		}

        PresignedUploadReq presignedUploadReq = new PresignedUploadReq();
        presignedUploadReq.setBasePath("adminapi");
        presignedUploadReq.setFileName(originalFilename);
        PresignedUploadResp presignedUploadResp = fileFeign.presignedUpload(presignedUploadReq);
        String fileKey = presignedUploadResp.getFileKey();
        String presignedUrl = presignedUploadResp.getPresignedUrl();

        byte[] fileBytes;
        try (InputStream inputStream = file.getInputStream()) {
            fileBytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("IOException", e);
            ExceptionUtil.throwException("文件上传失败");
            return null;
        }

        // 客户端使用presignedUrl上传文件
        HttpRequest httpRequest = HttpRequest.put(presignedUrl)
                // 设置不要自动添加Content-Type，否则会报签名不匹配
                .setConfig(HttpConfig.create().setUseDefaultContentTypeIfNull(false)).body(fileBytes);
        try (HttpResponse response = httpRequest.execute()) {
            String result = response.body();
            log.info("result:{}", result);
            return Collections.singletonMap("value", fileKey);
        }
    }

    @PostMapping("/presignedUpload")
    public PresignedUploadResp presignedUpload(String fileName) {
        PresignedUploadReq presignedUploadReq = new PresignedUploadReq();
        presignedUploadReq.setBasePath("adminapi");
        presignedUploadReq.setFileName(fileName);
        return fileFeign.presignedUpload(presignedUploadReq);
    }

    /**
     * 获取访问链接
     *
     * @param fileKey
     * @return
     */
	@GetMapping("/url")
	public Map<String, String> url(String fileKey) {
        return fileFeign.presignedUrl(fileKey);
	}

	/**
	 * 下载文件（302 到文件服务的预签名地址，供 img/超链接直接引用）
	 *
	 * @param fileKey 文件标识
	 * @param response 响应
	 */
	@GetMapping("/download")
	public void download(String fileKey, HttpServletResponse response) throws IOException {
		if (StringUtils.isBlank(fileKey)) {
			ExceptionUtil.throwException("文件标识不能为空");
		}
		Map<String, String> urlResult = fileFeign.presignedUrl(fileKey);
		String url = urlResult == null ? null : urlResult.get("value");
		if (StringUtils.isBlank(url)) {
			ExceptionUtil.throwException("文件不存在");
		}
		response.sendRedirect(url);
	}
}