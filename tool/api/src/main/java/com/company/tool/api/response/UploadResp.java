package com.company.tool.api.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadResp {
	/**
	 * 域名访问地址(推荐存储，后续使用presignedUrl获取访问链接)
	 */
	String fileKey;

	/**
	 * 域名访问地址(推荐使用域名)
	 */
	String domainUrl;

	/**
	 * oss访问地址
	 */
	String ossUrl;
}
