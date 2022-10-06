package com.company.web.file.alioss;

import java.io.InputStream;

import com.company.web.file.FileStorage;
import com.company.web.file.alioss.util.AliossUtil;

/**
 * 阿里云OSS
 * 
 * @author JQ棣
 *
 */
public class AliossFileStorage implements FileStorage {
	private String bucketName = null;
	
	public AliossFileStorage(String endpoint, String accessKeyId, String secretAccessKey, String bucketName) {
		AliossUtil.init(endpoint, accessKeyId, secretAccessKey);
		this.bucketName = bucketName;
	}

	@Override
	public String upload(InputStream inputStream, String fileName) {
		return AliossUtil.putObject(bucketName, fileName, inputStream);
	}

	@Override
	public InputStream download(String fileName) {
		return AliossUtil.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		AliossUtil.deleteObject(bucketName, fileName);
	}
}