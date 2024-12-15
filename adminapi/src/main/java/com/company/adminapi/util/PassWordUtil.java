package com.company.adminapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.crypto.SecureUtil;

/**
 * 密码工具类
 */
public class PassWordUtil {
	/**
	 * <pre>
	(?=.*[0-9])：至少包含一个数字。
	(?=.*[a-z])：至少包含一个小写字母。
	(?=.*[A-Z])：至少包含一个大写字母。
	(?=.*[`!@#$%^&*+=?])：至少包含一个特殊字符。
	(?=\\S+$)：密码不能包含空格。
	.{8,}：密码的长度至少为8个字符。
	 * </pre>
	 */
	private static final String COMPLEXITY_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[`!@#$%^&*+=?])(?=\\S+$).{8,}$";

	private PassWordUtil() {
	}

	public static String md5(String password) {
		return SecureUtil.md5(password);
	}

	/**
	 * 复杂度校验
	 * 
	 * @param password
	 * @return
	 */
	public static boolean complexityValidate(String password) {
		Pattern pattern = Pattern.compile(COMPLEXITY_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static void main(String[] args) {
		System.out.println(PassWordUtil.complexityValidate("2aZ&111"));
		System.out.println(PassWordUtil.complexityValidate("2aZ&1111"));
		System.out.println(PassWordUtil.complexityValidate("2aZ& 1111"));
		System.out.println(PassWordUtil.complexityValidate("2aZA1111"));
		System.out.println(PassWordUtil.complexityValidate("2aZA1111*"));
	}
}