package com.company.tool.file.alioss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.VoidResult;
import com.company.common.util.JsonUtil;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云OSS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class AliossClient {

	private OSS client = null;
	private String endpoint = null;

	public AliossClient(String endpoint, String accessKey, String secretKey) {
		CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKey, secretKey);
		this.client = OSSClientBuilder.create().endpoint(endpoint).credentialsProvider(credentialsProvider).build();
		this.endpoint = endpoint;
	}

	public String putObject(String bucketName, String key, InputStream input) {

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input);
		
		putObjectRequest.setProgressListener(progressEvent -> {
			long bytes = progressEvent.getBytes();
			ProgressEventType eventType = progressEvent.getEventType();
			log.info("bytes:{},eventType:{}", bytes, eventType);
		});
		
		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getETag();
		String versionId = putObjectResult.getVersionId();
		String requestId = putObjectResult.getRequestId();
		Long clientCRC = putObjectResult.getClientCRC();
		Long serverCRC = putObjectResult.getServerCRC();
		
		String uri = Optional.ofNullable(putObjectResult.getResponse()).map(ResponseMessage::getUri).orElse(null);
		Map<String, String> headers = Optional.ofNullable(putObjectResult.getResponse())
				.map(ResponseMessage::getHeaders).orElse(null);
		log.info("eTag:{},versionId:{},requestId:{},clientCRC:{},serverCRC:{},uri:{},headers:{}", eTag, versionId,
				requestId, clientCRC, serverCRC, uri, JsonUtil.toJsonString(headers));
		
		// 文件URL的格式为https://BucketName.Endpoint/ObjectName
		String url = String.format("https://%s.%s/%s", bucketName, endpoint, key);
		log.info("url:{}", url);
		return url;
	}

	public InputStream getObject(String bucketName, String key) {
		OSSObject ossObject = client.getObject(bucketName, key);
		
		String requestId = ossObject.getRequestId();
		Long clientCRC = ossObject.getClientCRC();
		Long serverCRC = ossObject.getServerCRC();

		ResponseMessage response = ossObject.getResponse();
		String uri = response.getUri();
		Map<String, String> headers = response.getHeaders();
		log.info("bucketName:{},key:{},requestId:{},clientCRC:{},serverCRC:{},uri:{},headers:{}", ossObject.getBucketName(), ossObject.getKey(),
				requestId, clientCRC, serverCRC, uri, JsonUtil.toJsonString(headers));
		
		InputStream inputStream = ossObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		VoidResult voidResult = client.deleteObject(bucketName, key);
		
		String requestId = voidResult.getRequestId();
		Long clientCRC = voidResult.getClientCRC();
		Long serverCRC = voidResult.getServerCRC();

		ResponseMessage response = voidResult.getResponse();
		String uri = response.getUri();
		Map<String, String> headers = response.getHeaders();
		log.info("requestId:{},clientCRC:{},serverCRC:{},uri:{},headers:{}", requestId, clientCRC, serverCRC, uri,
				JsonUtil.toJsonString(headers));

	}
	
	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "oss-cn-shenzhen.aliyuncs.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";
		
		AliossClient ossClient = new AliossClient(endpoint, accessKey, secretKey);
		
		String bucketName = "aocai-home";
		String key = "image/111.jpg";
		
		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = ossClient.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);
		System.out.println("略缩url:" + (url + "?x-oss-process=image/resize,m_lfit,h_60,w_60"));
		
		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/alioss-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
		
		/*
		// 删除文件
		ossTool.deleteObject(bucketName, key);
		*/
	}
}