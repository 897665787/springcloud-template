package com.company.tool.innercallback.processor.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProcessorBeanName {
	/**
	 * 请求之前
	 */
	private String beforeRequest;

	/**
	 * 响应成功
	 */
	private String returnSuccess;

	/**
	 * 放弃请求
	 */
	private String abandonRequest;
}
