package com.company.tool.file.baidubos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.baidubce.Protocol;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.BosObjectInputStream;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.GetObjectRequest;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectRequest;
import com.baidubce.services.bos.model.PutObjectResponse;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 百度BOS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class BaiduBosClient {

	private BosClient client = null;
	private String endpoint = null;
	
	public BaiduBosClient(String endpoint, String accessKey, String secretKey) {
		BosClientConfiguration config = new BosClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(accessKey, secretKey));
		config.setEndpoint(endpoint);
		config.setProtocol(Protocol.HTTPS);
		client = new BosClient(config);
		
		this.endpoint = endpoint;
	}

	public String putObject(String bucketName, String key, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input, metadata);

//		putObjectRequest.setProgressCallback();

		PutObjectResponse putObjectResponse = client.putObject(putObjectRequest);
		String eTag = putObjectResponse.getETag();
		log.info("eTag:{}", eTag);
		
		// 文件URL的格式为https://BucketName.Endpoint/ObjectName
		String url = String.format("https://%s.%s/%s", bucketName, endpoint, key);
		log.info("url:{}", url);
		
		return url;
	}

	public InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		BosObject bosObject = client.getObject(getObjectRequest);

		log.info("bucketName:{},key:{}", bosObject.getBucketName(), bosObject.getKey());

		BosObjectInputStream inputStream = bosObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}

	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "ap-guangzhou.baidu.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";
		
		BaiduBosClient ossClient = new BaiduBosClient(endpoint, accessKey, secretKey);

		String bucketName = "aocai-home";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = ossClient.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/baidubos-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
*/
	}
}