package com.company.admin.util;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	public Utils() {
	}

	public boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	public String toStr(String str) {
		return "<li>可以输出html：" + str+"</li>";
	}
}
