package com.company.web.file;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.web.file.alioss.AliossFileStorage;
import com.company.web.file.alioss.AliossProperties;
import com.company.web.file.minio.MinioFileStorage;
import com.company.web.file.minio.MinioProperties;

@Configuration
@ConditionalOnProperty(prefix = "template", name = "filestorage.active")
@EnableConfigurationProperties({ MinioProperties.class, AliossProperties.class })
public class FileStorageAutoConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "minio")
	FileStorage minioFileStorage(MinioProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		
		FileStorage fileStorage = new MinioFileStorage(endpoint, accessKey, secretKey, bucketName);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "alioss")
	FileStorage aliossFileStorage(AliossProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKeyId = properties.getAccessKeyId();
		String secretAccessKey = properties.getSecretAccessKey();
		String bucketName = properties.getBucketName();
		
		FileStorage fileStorage = new AliossFileStorage(endpoint, accessKeyId, secretAccessKey, bucketName);
		return fileStorage;
	}
}
