package com.company.tool.file.wangyinos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "template.filestorage.wangyinos")
public class WangyinosProperties {
	private String endpoint;
	private String accessKey;
	private String secretKey;
	private String bucketName;
	private String domain;
}
