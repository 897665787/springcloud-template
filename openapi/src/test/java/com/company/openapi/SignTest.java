package com.company.openapi;

import java.util.Map;

import com.company.framework.util.JsonUtil;
import com.company.openapi.util.SignUtil;
import com.google.common.collect.Maps;

public class SignTest {

	public static void main(String[] args) {
		String appid = "13532515";
		String appsecret = "b5d89c15161240d5ac17e846ab146721";

		// long timestamp = System.currentTimeMillis();
		long timestamp = 1690617367581L;
		System.out.println(timestamp);

		// String noncestr = RandomUtil.randomString(10);
		String noncestr = "5dx7lq57jn";
		System.out.println(noncestr);

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("orderCode", "222222222222222");

		Map<String, Object> bodyMap = Maps.newHashMap();
		bodyMap.put("orderCode", "3213213115116516351");
		String bodyStr = JsonUtil.toJsonString(bodyMap);
		// String bodyStr = "";

		String sign = SignUtil.generate(appid, timestamp, noncestr, paramMap, bodyStr, appsecret);
		System.out.println(sign);
	}
}
