package com.company.web.file.minio;

import java.io.InputStream;

import com.company.web.file.FileStorage;
import com.company.web.file.minio.util.MinioUtil;

/**
 * minio
 * 
 * @author JQ棣
 *
 */
public class MinioFileStorage implements FileStorage {

	private String bucketName;
	
	public MinioFileStorage(String endpoint, String accessKey, String secretKey, String bucketName) {
		MinioUtil.init(endpoint, accessKey, secretKey);
		this.bucketName = bucketName;
	}

	@Override
	public String upload(InputStream inputStream, String fileName) {
		return MinioUtil.putObject(bucketName, inputStream, fileName);
	}

	@Override
	public InputStream download(String fileName) {
		return MinioUtil.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		MinioUtil.removeObject(bucketName, fileName);
	}
}