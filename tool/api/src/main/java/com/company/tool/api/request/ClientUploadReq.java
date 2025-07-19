package com.company.tool.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class ClientUploadReq {
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
