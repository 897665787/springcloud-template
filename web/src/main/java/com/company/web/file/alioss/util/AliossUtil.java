package com.company.web.file.alioss.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.VoidResult;
import com.company.common.util.JsonUtil;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliossUtil {

	private static OSS client = null;
	
	private AliossUtil(){}

	public static void init(String endpoint, String accessKeyId, String secretAccessKey) {
		CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, secretAccessKey);
		client = OSSClientBuilder.create().endpoint(endpoint).credentialsProvider(credentialsProvider).build();
	}

	public static String putObject(String bucketName, String key, InputStream input) {
		PutObjectResult putObjectResult = client.putObject(bucketName, key, input);
		
		String eTag = putObjectResult.getETag();
		String versionId = putObjectResult.getVersionId();
		String requestId = putObjectResult.getRequestId();
		Long clientCRC = putObjectResult.getClientCRC();
		Long serverCRC = putObjectResult.getServerCRC();

		ResponseMessage response = putObjectResult.getResponse();
		String uri = response.getUri();
		Map<String, String> headers = response.getHeaders();
		log.info("eTag:{},versionId:{},requestId:{},clientCRC:{},serverCRC:{},uri:{},headers:{}", eTag, versionId,
				requestId, clientCRC, serverCRC, uri, JsonUtil.toJsonString(headers));

		return uri;
	}

	public static InputStream getObject(String bucketName, String key) {
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

	public static void deleteObject(String bucketName, String key) {
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
		String accessKeyId = "LTAIkcl1bVhsEpGf";
		String secretAccessKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";
					
		AliossUtil.init(endpoint, accessKeyId, secretAccessKey);
		
		String bucketName = "aocai-home";
		String key = "image/201904120816011555071361409.png";
		/*
		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		AliossUtil.putObject(bucketName, key, inputStream);
		*/
		
		// 下载文件
		InputStream download = AliossUtil.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/alioss-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
		
		/*
		// 删除文件
		AliossUtil.deleteObject(bucketName, key);
		*/
	}
}