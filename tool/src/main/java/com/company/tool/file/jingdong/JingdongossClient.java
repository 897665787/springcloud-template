package com.company.tool.file.jingdong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 京东云客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class JingdongossClient {

	private AmazonS3 client = null;

	public JingdongossClient(String endpoint, String accessKey, String secretKey) {
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
		builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "CHINA-HK"));
		client = builder.build();
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
		
		log.info("eTag:{},versionId:{}", eTag, versionId);
		
		URL URL = client.getUrl(bucketName, key);
		String url = URL.toString();
		log.info("url:{}", url);
		return url;
	}

	public InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		S3Object s3Object = client.getObject(getObjectRequest);
		log.info("bucketName:{},key:{}", s3Object.getBucketName(), s3Object.getKey());

		InputStream inputStream = s3Object.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}

	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "jingdong.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";

		JingdongossClient ossClient = new JingdongossClient(endpoint, accessKey, secretKey);

		String bucketName = "aocai-home";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = ossClient.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/jingdong-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
*/
	}
}