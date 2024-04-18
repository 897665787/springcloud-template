package com.company.framework.context;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.Platform;
import cn.hutool.http.useragent.UserAgent;

public class UserAgentUtil {

	private UserAgentUtil() {
	}

	public static UserAgentContext parse(String userAgentString) {
		UserAgentContext context = new UserAgentContext();
		if (StringUtils.isBlank(userAgentString)) {
			return context;
		}

		UserAgent userAgent = cn.hutool.http.useragent.UserAgentUtil.parse(userAgentString);
		if (userAgent != null) {
			Platform platform = userAgent.getPlatform();
			if (platform != null && !Platform.Unknown.equals(platform)) {
				if (platform.isAndroid()) {
					// context.setPlatform("");
					context.setOperator("android");
					// context.setChannel("");
				} else if (platform.isIos()) {
					// context.setPlatform("");
					context.setOperator("ios");
					// context.setChannel("");
				} else {
					String name = platform.getName();
					if ("Windows".equalsIgnoreCase(name)) {
						// context.setPlatform("");
						context.setOperator("win");
						// context.setChannel("");
					} else if ("Android".equalsIgnoreCase(name)) {
						// context.setPlatform("");
						context.setOperator("android");
						// context.setChannel("");
					} else if ("Mac".equalsIgnoreCase(name)) {
						// context.setPlatform("");
						context.setOperator("ios");
						// context.setChannel("");
					} else {
						// context.setPlatform("");
						context.setOperator(name);
						// context.setChannel("");
					}
				}
			}

			Browser browser = userAgent.getBrowser();
			if (browser != null && !Browser.Unknown.equals(browser)) {
				String name = browser.getName();
				if ("wxwork".equalsIgnoreCase(name) || "MicroMessenger".equalsIgnoreCase(name)
						|| "miniProgram".equalsIgnoreCase(name)) {
					context.setPlatform("mini");
					// context.setOperator("未知");
					context.setChannel(name);
				} else if ("Alipay".equalsIgnoreCase(name)) {
					context.setPlatform("alimini");
					// context.setOperator("未知");
					context.setChannel(name);
				} else {
					context.setPlatform(name);
					// context.setOperator("未知");
					context.setChannel(name);
				}
			}
		}
		if (userAgentString.contains("wechat") || userAgentString.contains("MicroMessenger")) {
			context.setPlatform("mini");
			// context.setOperator("");
			context.setChannel("wx");
		}

		if (userAgentString.contains("APP") || userAgentString.contains("app") || userAgentString.contains("App")) {
			context.setPlatform("app");
			// context.setOperator("");
			// context.setChannel("");
		}

		if (userAgentString.contains("iOS") || userAgentString.contains("iPhone") || userAgentString.contains("Mac")) {
			// context.setPlatform("");
			context.setOperator("ios");
			context.setChannel("ios");
		}

		if (userAgentString.contains("ANDROID") || userAgentString.contains("Android")
				|| userAgentString.contains("android")) {
			// context.setPlatform("");
			context.setOperator("android");
			// context.setChannel("ios");
		}

		if ("iOS".equalsIgnoreCase(userAgentString)) {
			context.setPlatform("app");
			context.setOperator("ios");
			context.setChannel("ios");
		} else if ("Android".equalsIgnoreCase(userAgentString)) {
			context.setPlatform("app");
			context.setOperator("android");
			// context.setChannel("");
		}

		if (userAgentString.contains("http-client")) {
			context.setPlatform("mini");
			context.setOperator("win");
			context.setChannel("http-client");
		}

		if (userAgentString.contains("Postman") || userAgentString.contains("postman")) { // 兜底
			context.setPlatform("mini");
			context.setOperator("win");
			context.setChannel("postman");
		}

		return context;
	}
}
