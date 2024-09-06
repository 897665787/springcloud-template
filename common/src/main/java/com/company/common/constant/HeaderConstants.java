package com.company.common.constant;

public interface HeaderConstants {
	// 公共请求头（与用户无关）
	String HEADER_PLATFORM = "x-platform";// 平台
	String HEADER_OPERATOR = "x-operator";// 操作系统
	String HEADER_VERSION = "x-version";// 版本号
	String HEADER_DEVICEID = "x-deviceid";// 设备ID
	String HEADER_CHANNEL = "x-channel";// 渠道
	String HEADER_REQUESTIP = "x-requestip";// 请求IP
	String HEADER_SOURCE = "x-source";// 请求来源
	// 国际化语言切换
	String ACCEPT_LANGUAGE = "Accept-Language";// 当前语言
	
	// 用户请求头（注：为了防止直接在header设置用户ID，绕过认证，要取最后1个值）
	String HEADER_CURRENT_USER_ID = "x-current-user-id";// 当前登录用户id
}
