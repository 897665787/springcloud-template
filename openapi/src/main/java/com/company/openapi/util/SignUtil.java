package com.company.openapi.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.company.common.util.JsonUtil;
import com.google.common.collect.Maps;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 签名算法：md5（appid + timestamp + noncestr + 业务参数（key字典排序） + appsecret）
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class SignUtil {

	private SignUtil() {
	}

	/**
	 * 生成签名：md5（appid + timestamp + noncestr + 业务参数（key字典排序） + appsecret）
	 */
	public static String generate(String appid, long timestamp, String noncestr, Map<String, Object> paramMap,
			String bodyStr, String appsecret) {
		TreeMap<String, Object> sortParamMap = MapUtil.sort(paramMap);
		String sortSourceStr = sortParamMap.entrySet().stream()
				.filter(v -> StringUtils.isNotBlank(v.getKey()) && v.getValue() != null)
				.map(v -> v.getKey() + "=" + v.getValue()).collect(Collectors.joining("&"));
		String md5SourceStr = appid + timestamp + noncestr + sortSourceStr + bodyStr + appsecret;
		String md5 = SecureUtil.md5(md5SourceStr).toUpperCase();
		log.info("md5SourceStr:{},md5:{}", md5SourceStr, md5);
		return md5;
	}

	public static void main(String[] args) {
		String appid = "13532515";
		String appsecret = "b5d89c15161240d5ac17e846ab146721";
		
//		long timestamp = System.currentTimeMillis();
		long timestamp = 1690617367581L;
		System.out.println(timestamp);
		
//		String noncestr = RandomUtil.randomString(10);
		String noncestr = "5dx7lq57jn";
		System.out.println(noncestr);

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("orderCode", "222222222222222");

		Map<String, Object> bodyMap = Maps.newHashMap();
		bodyMap.put("orderCode", "3213213115116516351");
		String bodyStr = JsonUtil.toJsonString(bodyMap);
//		String bodyStr = "";

		String sign = SignUtil.generate(appid, timestamp, noncestr, paramMap, bodyStr, appsecret);
		System.out.println(sign);
	}
}