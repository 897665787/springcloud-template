package com.company.tool.file.jingdong;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "template.filestorage.jingdongoss")
public class JingdongossProperties {
	private String endpoint;
	private String accessKey;
	private String secretKey;
	private String bucketName;
	private String domain;
}
