package com.company.tool.file.alioss;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "template.filestorage.alioss")
public class AliossProperties {
	private String endpoint;
	private String accessKeyId;
	private String secretAccessKey;
	private String bucketName;
}
