package com.company.tool.file.local;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "template.filestorage.local")
public class LocalProperties {
	private String endpoint;
	private String bucketName;
	private String domain;
}
