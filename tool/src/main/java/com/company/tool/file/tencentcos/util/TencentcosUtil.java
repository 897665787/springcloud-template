package com.company.tool.file.tencentcos.util;

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

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯云COS
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class TencentcosUtil {

	private static COSClient client = null;

	private TencentcosUtil() {
	}

	public static void init(String endpoint, String accessKeyId, String secretAccessKey) {
		COSCredentials cred = new BasicCOSCredentials(accessKeyId, secretAccessKey);

		// ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));

		ClientConfig clientConfig = new ClientConfig();
		UserSpecifiedEndpointBuilder endpointBuilder = new UserSpecifiedEndpointBuilder(endpoint, endpoint);
		clientConfig.setEndpointBuilder(endpointBuilder);

		client = new COSClient(cred, clientConfig);
	}

	public static String putObject(String bucketName, String key, InputStream input) {
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

	public static InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		COSObject cosObject = client.getObject(getObjectRequest);

		ObjectMetadata objectMetadata = cosObject.getObjectMetadata();
		String requestId = objectMetadata.getRequestId();

		log.info("bucketName:{},key:{},requestId:{}", cosObject.getBucketName(), cosObject.getKey(), requestId);

		COSObjectInputStream inputStream = cosObject.getObjectContent();
		return inputStream;
	}

	public static void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}

	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "ap-guangzhou.myqcloud.com";
		String accessKeyId = "LTAIkcl1bVhsEpGf";
		String secretAccessKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";

		TencentcosUtil.init(endpoint, accessKeyId, secretAccessKey);

		String bucketName = "aocai-home";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = TencentcosUtil.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
		InputStream download = TencentcosUtil.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/tencentcos-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
/*
		// 删除文件
		TencentcosUtil.deleteObject(bucketName, key);
*/
	}
}