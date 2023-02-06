package com.company.tool.file.baidubos;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.company.tool.file.FileStorage;
import com.company.tool.file.FileUrl;

/**
 * 百度BOS
 * 
 * @author JQ棣
 *
 */
public class BaiduBosFileStorage implements FileStorage {
	private BaiduBosClient ossClient = null;
	private String bucketName = null;
	private String domain = null;
	
	public BaiduBosFileStorage(String endpoint, String accessKey, String secretKey, String bucketName, String domain) {
		this.ossClient = new BaiduBosClient(endpoint, accessKey, secretKey);
		this.bucketName = bucketName;
		this.domain = domain;
	}

	@Override
	public FileUrl upload(InputStream inputStream, String fileName) {
		String ossUrl = ossClient.putObject(bucketName, fileName, inputStream);
		FileUrl fileUrl = new FileUrl();
		fileUrl.setOssUrl(ossUrl);
		if (StringUtils.isNotBlank(domain)) {
			String domainUrl = String.format("%s/%s", domain, fileName);
			fileUrl.setDomainUrl(domainUrl);
		} else {
			fileUrl.setDomainUrl(ossUrl);
		}
		return fileUrl;
	}

	@Override
	public InputStream download(String fileName) {
		return ossClient.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		ossClient.deleteObject(bucketName, fileName);
	}
}