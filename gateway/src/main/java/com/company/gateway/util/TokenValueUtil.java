package com.company.gateway.util;

import org.apache.commons.lang3.StringUtils;

/**
 * token工具类
 */
public class TokenValueUtil {

	private TokenValueUtil() {
	}

	public static String fixToken(String prefix, String headerToken) {
		if (StringUtils.isBlank(headerToken)) {
			return null;
		}
		String tokenStr = headerToken;
		
		if (StringUtils.isNotBlank(prefix)) {
			if (!headerToken.startsWith(prefix)) {
				return null;
			}
			tokenStr = headerToken.replaceFirst(prefix, "").trim();
			if (StringUtils.isBlank(tokenStr)) {
				return null;
			}
		}

		if ("undefined".equals(tokenStr)) {
			return null;
		}

		if ("null".equals(tokenStr)) {
			return null;
		}

		if ("(null)".equals(tokenStr)) {
			return null;
		}
		return tokenStr;
	}
}