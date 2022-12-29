package com.company.tool.file.tencentcos;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.company.tool.file.FileStorage;
import com.company.tool.file.FileUrl;

/**
 * 腾讯云COS
 * 
 * @author JQ棣
 *
 */
public class TencentcosFileStorage implements FileStorage {
	private TencentcosClient client = null;
	private String bucketName = null;
	private String domain = null;
	
	public TencentcosFileStorage(String endpoint, String accessKey, String secretKey, String bucketName, String domain) {
		this.client = new TencentcosClient(endpoint, accessKey, secretKey);
		this.bucketName = bucketName;
		this.domain = domain;
	}

	@Override
	public FileUrl upload(InputStream inputStream, String fileName) {
		String ossUrl = client.putObject(bucketName, fileName, inputStream);
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
		return client.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		client.deleteObject(bucketName, fileName);
	}
}