package com.company.admin.util;

import cn.hutool.core.util.RandomUtil;

public class InviteCodeUtil {
	/**
	 * 生成随机码
	 *
	 * @return 随机码
	 */
	public static String generateCode() {
		return RandomUtil.randomStringUpper(6);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(RandomUtil.randomStringUpper(6));
		}
	}
}
