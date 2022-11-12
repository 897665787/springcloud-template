package com.company.tool.file.tencentcos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "template.filestorage.tencentcos")
public class TencentcosProperties {
	private String endpoint;
	private String secretId;
	private String secretKey;
	private String bucketName;
}
