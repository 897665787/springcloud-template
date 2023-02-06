package com.company.tool.file.wangyinos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.NOSObject;
import com.netease.cloud.services.nos.model.NOSObjectInputStream;
import com.netease.cloud.services.nos.model.PutObjectRequest;
import com.netease.cloud.services.nos.model.PutObjectResult;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 网易NOS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class WangyinosClient {

	private NosClient client = null;
	private String endpoint = null;

	public WangyinosClient(String endpoint, String accessKey, String secretKey) {
		Credentials credentials = new BasicCredentials(accessKey, secretKey);
		this.client = new NosClient(credentials);
		this.endpoint = endpoint;
	}

	public String putObject(String bucketName, String key, InputStream input) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input, null);

		putObjectRequest.setProgressListener(progressEvent -> {
			int bytes = progressEvent.getBytesTransfered();
			int eventCode = progressEvent.getEventCode();
			log.info("bytes:{},eventCode:{}", bytes, eventCode);
		});
		
		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getETag();
		String versionId = putObjectResult.getVersionId();
		log.info("eTag:{},versionId:{}", eTag, versionId);

		// 文件URL的格式为https://BucketName.Endpoint/ObjectName
		String url = String.format("https://%s.%s/%s", bucketName, endpoint, key);
		log.info("url:{}", url);
		return url;
	}

	public InputStream getObject(String bucketName, String key) {
		NOSObject nosObject = client.getObject(bucketName, key);
		NOSObjectInputStream inputStream = nosObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "oss-cn-shenzhen.aliyuncs.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";
		
		WangyinosClient ossClient = new WangyinosClient(endpoint, accessKey, secretKey);
		
		String bucketName = "aocai-home";
		String key = "image/111.jpg";
		
		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = ossClient.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);
		
		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/wangyinos-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
		
		/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
		*/
	}
}