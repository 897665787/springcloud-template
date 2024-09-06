package com.company.adminapi.filestorage;

import java.io.InputStream;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传
 */
@Slf4j
@Service
public class UploadService {

	@Autowired
	private FileStorage fileStorage;
	
	public String upload(InputStream inputStream, String fileName) {
		if (StringUtils.isBlank(fileName)) {
			byte[] bytes = IoUtil.readBytes(inputStream);
			fileName = String.format("%s.%s", "uuid-replace", Utils.extraSuffix(bytes));
		}
		
		fileName = generateFileName(fileName);
		String fullFileName = fullFileName("template", fileName);
		
		FileUrl fileUrl = fileStorage.upload(inputStream, fullFileName);
		
		log.info("fileUrl:{}", JsonUtil.toJsonString(fileUrl));
		return fileUrl.getDomainUrl();
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
