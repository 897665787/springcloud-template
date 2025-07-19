package com.company.tool.api.request;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class BannerReq {
	/* 运行时参数 */
	/**
	 * 运行时附加值
	 */
	Map<String, String> runtimeAttach = new HashMap<>();
}
