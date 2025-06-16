package com.company.tool.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploadReq {

	@NotNull
	private byte[] bytes;

	/**
	 * 根目录<非必填>
	 */
	private String basePath = "";

	/**
	 * 文件名
	 */
	@NotBlank(message = "文件名不能为空")
	private String fileName;
}
