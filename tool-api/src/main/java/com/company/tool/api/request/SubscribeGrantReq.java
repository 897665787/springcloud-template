package com.company.tool.api.request;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class SubscribeGrantReq {
	String openid;
	String group;
	String resJson;

	/* 运行时参数 */
	/**
	 * 运行时附加值
	 */
	Map<String, String> runtimeAttach = new HashMap<>();
}
