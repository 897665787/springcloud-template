package com.company.framework.context;

import java.util.List;

import com.google.common.collect.Lists;

import cn.hutool.json.JSONUtil;

public class UserAgentUtilTest {

	public static void main(String[] args) {
		List<String> dataList = Lists.newArrayList();
		// 开发者工具 iphone
		dataList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1 wechatdevtools/1.02.2004020 MicroMessenger/7.0.4 Language/zh_CN webview/");
		// 开发者工具 nexus
		dataList.add("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Mobile Safari/537.36 wechatdevtools/1.02.2004020 MicroMessenger/7.0.4 webview/");
		// 小程序 iphone
		dataList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 16_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.48(0x1800302b) NetType/WIFI Language/zh_CN");
		// app iphone 配送端 iOS
		dataList.add("iOS");
		// app iphone 配送端 Android
		dataList.add("Android");
		dataList.add("Mozilla/5.0 (Linux; Android 14; 23113RKC6C Build/UKQ1.230804.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/116.0.0.0 Mobile Safari/537.36 XWEB/1160083 MMWEBSDK/20240301 MMWEBID/5698 MicroMessenger/8.0.48.2580(0x28003039) WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64 MiniProgramEnv/android");
		dataList.add("PostmanRuntime/7.26.8");
		dataList.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Windows WindowsWechat/WMPF WindowsWechat(0x6309092b) XWEB/8555");
		
		for (String userAgentString : dataList) {
			UserAgentContext parse = UserAgentUtil.parse(userAgentString);
			System.out.println(JSONUtil.toJsonPrettyStr(parse));
		}
	}
}
