package com.company.tool.nav;

import java.util.Map;

public interface NavReplaceParam {

	/**
	 * 替换参数
	 * 
	 * <pre>
	 * tips：暂时没想好应该接收哪些参数，后续可按需添加参数
	 * </pre>
	 */
	Map<String, String> replace(Map<String, String> attachMap);
}