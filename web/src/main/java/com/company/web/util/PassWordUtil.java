package com.company.web.util;

import cn.hutool.crypto.SecureUtil;

/**
 * 密码工具类
 */
public class PassWordUtil {
	private PassWordUtil() {
	}

	public static String md5(String password) {
		return SecureUtil.md5(password);
	}
}