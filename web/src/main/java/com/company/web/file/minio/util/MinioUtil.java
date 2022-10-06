package com.company.web.file.minio.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;

@Slf4j
public class MinioUtil {

	private static MinioClient client = null;
	
	private MinioUtil(){}

	public static void init(String endpoint, String accessKey, String secretKey) {
		client = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
	}

	public static String putObject(String bucketName, InputStream stream, String objectName) {
		long objectSize = 0L;
		try {
			objectSize = stream.available();
		} catch (IOException e) {
			log.error("inputStream.available error", e);
		}

		long partSize = -1L;
		PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
				// 文件大小objectSize、分片大小partSize，分片数我们传入的是-1，表示使用默认配置
				/**
				 * <pre>
				 * 分片大小不能小于5MB，大于5GB
				 * 对象大小不能超过5TiB
				 * partSize传入-1，默认按照5MB进行分割
				 * 分片数量不能超过10000
				 * </pre>
				 */
				.stream(stream, objectSize, partSize).build();

		try {
			ObjectWriteResponse objectWriteResponse = client.putObject(putObjectArgs);
			String etag = objectWriteResponse.etag();
			String versionId = objectWriteResponse.versionId();
			Headers headers = objectWriteResponse.headers();
			String bucket = objectWriteResponse.bucket();
			String region = objectWriteResponse.region();
			String object = objectWriteResponse.object();

			log.info("etag:{},versionId:{},headers:{},bucket:{},region:{},object:{}", etag, versionId,
					headers.toString(), bucket, region, object);
		} catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
				| InternalException | InvalidBucketNameException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | IOException e) {
			log.error("minioClient.putObject error", e);
		}

		String url = null;
		try {
			String objectUrl = client.getObjectUrl(bucketName, objectName);
			log.info("objectUrl:{}", objectUrl);
			url = objectUrl;
		} catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
				| InternalException | InvalidBucketNameException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | IOException e) {
			log.error("minioClient.getObjectUrl error", e);
		}
		
		GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder().method(Method.GET)
				.bucket(bucketName).object(objectName).build();
		try {
			String presignedObjectUrl = client.getPresignedObjectUrl(getPresignedObjectUrlArgs);
			log.info("presignedObjectUrl:{}", presignedObjectUrl);
		} catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
				| InternalException | InvalidBucketNameException | InvalidExpiresRangeException
				| InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException
				| IOException e) {
			log.error("minioClient.getPresignedObjectUrl error", e);
		}

		return url;
	}

	public static InputStream getObject(String bucketName, String objectName) {
		GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();

		InputStream inputStream = null;
		try {
			inputStream = client.getObject(objectArgs);
		} catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
				| InternalException | InvalidBucketNameException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | IOException e) {
			log.error("minioClient.getObject error", e);
		}

		return inputStream;
	}

	public static void removeObject(String bucketName, String objectName) {
		RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
		try {
			client.removeObject(removeObjectArgs);
		} catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
				| InternalException | InvalidBucketNameException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | IOException e) {
			log.error("minioClient.removeObject error", e);
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		String endpoint = "http://127.0.0.1:9000";
		String accessKey = "pRYo5fCm9ZyVNKLA";
		String secretKey = "Lv5yFUrBOXrNsZJVZ93XxSVl7JiNA99J";

		MinioUtil.init(endpoint, accessKey, secretKey);
		
		String bucketName = "buket-template";
		String objectName = "333.jpg";
		
		// 上传文件
		FileInputStream inputStream = IoUtil.toStream(new File("D:/111.jpg"));
		String url = MinioUtil.putObject(bucketName, inputStream, objectName);
		System.out.println("url:" + url);
		
		
		// 下载文件
		InputStream download = MinioUtil.getObject(bucketName, objectName);
		FastByteArrayOutputStream read = IoUtil.read(download);
		OutputStream out = new FileOutputStream(new File("D:/minio-download.jpg"));
		IoUtil.write(out, true, read.toByteArray());
		
		/*
		// 删除文件
		MinioUtil.removeObject(bucketName, objectName);
		*/
	}
}