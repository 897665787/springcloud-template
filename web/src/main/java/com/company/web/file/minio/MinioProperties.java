package com.company.web.file.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "template.filestorage.minio")
public class MinioProperties {
	private String endpoint;
	private String accessKey;
	private String secretKey;
	private String bucketName;
}