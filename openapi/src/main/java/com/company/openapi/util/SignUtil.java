package com.company.openapi.util;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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
	 * 生成签名
	 * 
	 * @param appid
	 *            appid
	 * @param timestamp
	 *            时间戳（毫秒）
	 * @param noncestr
	 *            随机串（建议至少10位）
	 * @param appsecret
	 *            appid对应的密钥
	 * @return
	 */
	public static String generate(String appid, long timestamp, String noncestr, String appsecret) {
		return generate(appid, timestamp, noncestr, Collections.emptyMap(), "", appsecret);
	}

	/**
	 * 生成签名
	 * 
	 * @param appid
	 *            appid
	 * @param timestamp
	 *            时间戳（毫秒）
	 * @param noncestr
	 *            随机串（建议至少10位）
	 * @param paramMap
	 *            url参数与form参数
	 * @param appsecret
	 *            appid对应的密钥
	 * @return
	 */
	public static String generate(String appid, long timestamp, String noncestr, Map<String, Object> paramMap,
			String appsecret) {
		return generate(appid, timestamp, noncestr, paramMap, "", appsecret);
	}

	/**
	 * 生成签名
	 * 
	 * @param appid
	 *            appid
	 * @param timestamp
	 *            时间戳（毫秒）
	 * @param noncestr
	 *            随机串（建议至少10位）
	 * @param bodyStr
	 *            body参数串
	 * @param appsecret
	 *            appid对应的密钥
	 * @return
	 */
	public static String generate(String appid, long timestamp, String noncestr, String bodyStr, String appsecret) {
		return generate(appid, timestamp, noncestr, Collections.emptyMap(), bodyStr, appsecret);
	}

	/**
	 * 生成签名
	 * 
	 * @param appid
	 *            appid
	 * @param timestamp
	 *            时间戳（毫秒）
	 * @param noncestr
	 *            随机串（建议至少10位）
	 * @param paramMap
	 *            url参数与form参数
	 * @param bodyStr
	 *            body参数串
	 * @param appsecret
	 *            appid对应的密钥
	 * @return
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

}