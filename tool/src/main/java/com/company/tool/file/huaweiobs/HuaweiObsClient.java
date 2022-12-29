package com.company.tool.file.huaweiobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.obs.services.ObsClient;
import com.obs.services.model.GetObjectRequest;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 华为云OBS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class HuaweiObsClient {

	private ObsClient client = null;

	public HuaweiObsClient(String endpoint, String accessKey, String secretKey) {
		client = new ObsClient(accessKey, secretKey, endpoint);
	}

	public String putObject(String bucketName, String objectKey, InputStream input) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, input);

		putObjectRequest.setProgressListener(progressEvent -> {
			long bytes = progressEvent.getTotalBytes();
			int transferPercentage = progressEvent.getTransferPercentage();
			log.info("bytes:{},transferPercentage:{}", bytes, transferPercentage);
		});

		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getEtag();
		String versionId = putObjectResult.getVersionId();
		String requestId = putObjectResult.getRequestId();
		String url = putObjectResult.getObjectUrl();
		
		log.info("eTag:{},versionId:{},requestId:{},url:{}", eTag, versionId, requestId, url);
		return url;
	}

	public InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		ObsObject obsObject = client.getObject(getObjectRequest);
		ObjectMetadata objectMetadata = obsObject.getMetadata();
		String requestId = objectMetadata.getRequestId();

		log.info("bucketName:{},key:{},requestId:{}", obsObject.getBucketName(), obsObject.getObjectKey(), requestId);

		InputStream inputStream = obsObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}

	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "ap-guangzhou.huawei.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";

		HuaweiObsClient ossClient = new HuaweiObsClient(endpoint, accessKey, secretKey);

		String bucketName = "aocai-home";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = ossClient.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/huaweiobs-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
*/
	}
}