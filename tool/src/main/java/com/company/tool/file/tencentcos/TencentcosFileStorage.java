package com.company.tool.file.tencentcos;

import java.io.InputStream;

import com.company.tool.file.FileStorage;
import com.company.tool.file.tencentcos.util.TencentcosUtil;

/**
 * 腾讯云COS
 * 
 * @author JQ棣
 *
 */
public class TencentcosFileStorage implements FileStorage {
	private String bucketName = null;
	
	public TencentcosFileStorage(String endpoint, String accessKeyId, String secretAccessKey, String bucketName) {
		TencentcosUtil.init(endpoint, accessKeyId, secretAccessKey);
		this.bucketName = bucketName;
	}

	@Override
	public String upload(InputStream inputStream, String fileName) {
		return TencentcosUtil.putObject(bucketName, fileName, inputStream);
	}

	@Override
	public InputStream download(String fileName) {
		return TencentcosUtil.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		TencentcosUtil.deleteObject(bucketName, fileName);
	}
}