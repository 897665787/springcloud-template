package com.company.tool.file.tencentcos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.endpoint.UserSpecifiedEndpointBuilder;
import com.qcloud.cos.event.ProgressEventType;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯云COS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class TencentcosClient {

	private COSClient client = null;

	public TencentcosClient(String endpoint, String accessKey, String secretKey) {
		COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);

		ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));

		UserSpecifiedEndpointBuilder endpointBuilder = new UserSpecifiedEndpointBuilder(endpoint, endpoint);
		clientConfig.setEndpointBuilder(endpointBuilder);

		client = new COSClient(cred, clientConfig);
	}

	public String putObject(String bucketName, String key, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input, metadata);

		putObjectRequest.setGeneralProgressListener(progressEvent -> {
			long bytes = progressEvent.getBytes();
			ProgressEventType eventType = progressEvent.getEventType();
			log.info("bytes:{},eventType:{}", bytes, eventType);
		});

		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getETag();
		String versionId = putObjectResult.getVersionId();
		String requestId = putObjectResult.getRequestId();

		log.info("eTag:{},versionId:{},requestId:{}", eTag, versionId, requestId);

		URL URL = client.getObjectUrl(bucketName, key);
		String url = URL.toString();
		log.info("url:{}", url);
		return url;
	}

	public InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		COSObject cosObject = client.getObject(getObjectRequest);

		ObjectMetadata objectMetadata = cosObject.getObjectMetadata();
		String requestId = objectMetadata.getRequestId();

		log.info("bucketName:{},key:{},requestId:{}", cosObject.getBucketName(), cosObject.getKey(), requestId);

		COSObjectInputStream inputStream = cosObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}

	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "ap-guangzhou.myqcloud.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";

		TencentcosClient ossClient = new TencentcosClient(endpoint, accessKey, secretKey);

		String bucketName = "aocai-home";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = ossClient.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/tencentcos-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
*/
	}
}