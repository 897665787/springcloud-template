package com.company.common.enums;

import java.util.function.Function;

import cn.hutool.core.util.DesensitizedUtil;

/**
 * 脱敏类型（可参考cn.hutool.core.util.DesensitizedUtil.DesensitizedType）
 * 
 * @author JQ棣
 */
public enum DesensitizedType {
	/**
	 * 姓名，第2位星号替换
	 */
	USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

	/**
	 * 密码，全部字符都用*代替
	 */
	PASSWORD(s -> DesensitizedUtil.desensitized(s, DesensitizedUtil.DesensitizedType.PASSWORD)),

	/**
	 * 身份证，保留前1位和后2位
	 */
	ID_CARD(s -> DesensitizedUtil.desensitized(s, DesensitizedUtil.DesensitizedType.ID_CARD)),

	/**
	 * 手机号，前三位，后4位，其他隐藏
	 */
	MOBILE(s -> DesensitizedUtil.desensitized(s, DesensitizedUtil.DesensitizedType.MOBILE_PHONE)),

	/**
	 * 电子邮箱，仅显示第一个字母和@后面的地址显示，其他星号替换
	 */
	EMAIL(s -> s.replaceAll("(^.)[^@]*(@.*$)", "$1****$2")),

	/**
	 * 银行卡号，保留最后4位，其他星号替换
	 */
	BANK_CARD(s -> DesensitizedUtil.desensitized(s, DesensitizedUtil.DesensitizedType.BANK_CARD)),
	;

	private final Function<String, String> desensitizer;

	DesensitizedType(Function<String, String> desensitizer) {
		this.desensitizer = desensitizer;
	}

	public Function<String, String> desensitizer() {
		return desensitizer;
	}

	public String desensitize(String value) {
		return desensitizer.apply(value);
	}
}
