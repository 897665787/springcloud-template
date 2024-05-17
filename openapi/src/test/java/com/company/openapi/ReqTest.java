package com.company.openapi;

import java.util.Map;

import com.company.common.util.JsonUtil;
import com.company.openapi.req.CreateOrderReq;
import com.company.openapi.util.SignUtil;
import com.google.common.collect.Maps;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;

public class ReqTest {
	static String appid = "13532515";
	static String appsecret = "b5d89c15161240d5ac17e846ab146721";

	public static void main(String[] args) {
		orderinfo();
		orderget();
		ordercreate();
		ordercreate2();
	}

	static void orderinfo() {
		String urlString = "http://localhost:6201/order/info";

		long timestamp = System.currentTimeMillis();
		String noncestr = RandomUtil.randomString(10);

		Map<String, String> headers = Maps.newHashMap();
		headers.put("appid", appid);
		headers.put("noncestr", noncestr);
		headers.put("timestamp", "" + timestamp);
		String sign = SignUtil.generate(appid, timestamp, noncestr, appsecret);
		headers.put("sign", sign);

		String result = HttpRequest.get(urlString).addHeaders(headers).execute().body();
		System.out.println(result);
	}

	static void orderget() {
		String orderCode = "12312345646";

		String urlString = "http://localhost:6201/order/get?orderCode=" + orderCode;

		long timestamp = System.currentTimeMillis();
		String noncestr = RandomUtil.randomString(10);

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("orderCode", orderCode);

		Map<String, String> headers = Maps.newHashMap();
		headers.put("appid", appid);
		headers.put("noncestr", noncestr);
		headers.put("timestamp", "" + timestamp);
		String sign = SignUtil.generate(appid, timestamp, noncestr, paramMap, appsecret);
		headers.put("sign", sign);

		String result = HttpRequest.get(urlString).addHeaders(headers).execute().body();
		System.out.println(result);
	}

	static void ordercreate() {
		String urlString = "http://localhost:6201/order/create";
		long timestamp = System.currentTimeMillis();
		String noncestr = RandomUtil.randomString(10);

		CreateOrderReq req = new CreateOrderReq();
		req.setOrderCode("12312345646");
		req.setProductCode("A555");
		String bodyStr = JsonUtil.toJsonString(req);

		Map<String, String> headers = Maps.newHashMap();
		headers.put("appid", appid);
		headers.put("noncestr", noncestr);
		headers.put("timestamp", "" + timestamp);
		String sign = SignUtil.generate(appid, timestamp, noncestr, bodyStr, appsecret);
		headers.put("sign", sign);

		String result = HttpRequest.post(urlString).body(bodyStr).addHeaders(headers).execute().body();
		System.out.println(result);
	}

	static void ordercreate2() {
		String urlString = "http://localhost:6201/order/create2";
		long timestamp = System.currentTimeMillis();
		String noncestr = RandomUtil.randomString(10);

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("orderCode", "111111111111111111");
		paramMap.put("productCode", "A333");

		Map<String, String> headers = Maps.newHashMap();
		headers.put("appid", appid);
		headers.put("noncestr", noncestr);
		headers.put("timestamp", "" + timestamp);
		String sign = SignUtil.generate(appid, timestamp, noncestr, paramMap, appsecret);
		headers.put("sign", sign);

		String result = HttpRequest.post(urlString).form(paramMap).addHeaders(headers).execute().body();
		System.out.println(result);
	}
}