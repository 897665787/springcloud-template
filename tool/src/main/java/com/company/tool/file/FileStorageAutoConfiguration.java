package com.company.tool.file;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.tool.file.alioss.AliossFileStorage;
import com.company.tool.file.alioss.AliossProperties;
import com.company.tool.file.amazons3.AmazonS3FileStorage;
import com.company.tool.file.amazons3.AmazonS3Properties;
import com.company.tool.file.baidubos.BaiduBosFileStorage;
import com.company.tool.file.baidubos.BaiduBosProperties;
import com.company.tool.file.huaweiobs.HuaweiObsFileStorage;
import com.company.tool.file.huaweiobs.HuaweiObsProperties;
import com.company.tool.file.jingdong.JingdongossFileStorage;
import com.company.tool.file.jingdong.JingdongossProperties;
import com.company.tool.file.local.LocalFileStorage;
import com.company.tool.file.local.LocalProperties;
import com.company.tool.file.minio.MinioFileStorage;
import com.company.tool.file.minio.MinioProperties;
import com.company.tool.file.tencentcos.TencentcosFileStorage;
import com.company.tool.file.tencentcos.TencentcosProperties;
import com.company.tool.file.wangyinos.WangyinosFileStorage;
import com.company.tool.file.wangyinos.WangyinosProperties;

@Configuration
@ConditionalOnProperty(prefix = "template", name = "filestorage.active")
@ConditionalOnMissingBean(FileStorage.class)
@EnableConfigurationProperties({ LocalProperties.class, MinioProperties.class, AliossProperties.class,
		TencentcosProperties.class, AmazonS3Properties.class, BaiduBosProperties.class, HuaweiObsProperties.class,
		WangyinosProperties.class, JingdongossProperties.class })
public class FileStorageAutoConfiguration {
	
	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "local")
	FileStorage localFileStorage(LocalProperties properties) {
		String endpoint = properties.getEndpoint();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new LocalFileStorage(endpoint, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "minio")
	FileStorage minioFileStorage(MinioProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new MinioFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "alioss")
	FileStorage aliossFileStorage(AliossProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new AliossFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "tencentcos")
	FileStorage tencentcosFileStorage(TencentcosProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new TencentcosFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "amazons3")
	FileStorage amazonS3FileStorage(AmazonS3Properties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new AmazonS3FileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "baidubos")
	FileStorage baiduBosFileStorage(BaiduBosProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new BaiduBosFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "huaweiobs")
	FileStorage huaweiObsFileStorage(HuaweiObsProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new HuaweiObsFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "wangyinos")
	FileStorage wangyinosFileStorage(HuaweiObsProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();
		
		FileStorage fileStorage = new WangyinosFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "template", name = "filestorage.active", havingValue = "jingdongoss")
	FileStorage jingdongossFileStorage(HuaweiObsProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();
		
		FileStorage fileStorage = new JingdongossFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}
}
