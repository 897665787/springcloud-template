package com.company.common.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;

public class Utils {
	
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String md5(String data) {
		return SecureUtil.md5(data);
	}
	
	/**
	 * 以/拼接remark(取左侧200字符)
	 * 
	 * @param oldRemark
	 * @param newRemark
	 * @return
	 */
	public static String appendRemark(String oldRemark, String newRemark) {
		if (StringUtils.isBlank(newRemark)) {
			return oldRemark;
		}
		String remark = newRemark;
		if (StringUtils.isNotBlank(oldRemark)) {
			remark = StringUtils.join(new String[] { oldRemark, remark }, "/");
		}
		remark = StringUtils.substring(remark, 0, 200);
		return remark;
	}
	
	/**
	 * 以/拼接remark(取右侧200字符)
	 * 
	 * @param oldRemark
	 * @param newRemark
	 * @return
	 */
	public static String rightRemark(String oldRemark, String newRemark) {
		if (StringUtils.isBlank(newRemark)) {
			return oldRemark;
		}
		String remark = newRemark;
		if (StringUtils.isNotBlank(oldRemark)) {
			remark = StringUtils.join(new String[] { oldRemark, remark }, "/");
		}
		remark = StringUtils.right(remark, 200);
		return remark;
	}

	/**
	 * 任意一个strs中的值包含searchStr
	 * 
	 * @param searchStr
	 * @param strs
	 * @return
	 */
	public static boolean anyContains(String searchStr, String... strs) {
		if (strs == null) {
			return false;
		}
		for (String str : strs) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			if (str.contains(searchStr)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 自动检测后缀
	 * 
	 * @param bytes
	 * @return
	 */
	public static String extraSuffix(byte[] bytes) {
		byte[] headByte = new byte[28];
		System.arraycopy(bytes, 0, headByte, 0, 28);
		String fileHexHead = HexUtil.encodeHexStr(headByte, false);
		return FileTypeUtil.getType(fileHexHead);
	}
	
	/**
	 * 
	 * 例子：com.company.order.mapper.AliPayMapper.selectByOutTradeNo
	 * 返回AliPayMapper.selectByOutTradeNo
	 * 
	 * @param str
	 * @return
	 */
	public static String mapperAndId(String str) {
		try {
			if (str == null) {
				return null;
			}
			int index = str.lastIndexOf(".");
			if (index == -1) {
				return str;
			}
			String substring = str.substring(0, index);
			int index2 = substring.lastIndexOf(".");
			if (index2 == -1) {
				return str;
			}
			return str.substring(index2 + 1);
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 将key:value添加到json字符串中（取使用getByJson）
	 * 
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return
	 */
	public static String append2Json(String jsonString, String key, String value) {
		if (StringUtils.isBlank(value)) {
			return jsonString;
		}
		ObjectNode objectNode = JsonUtil.toNotNullObjectNode(jsonString);
		objectNode.put(key, value);
		return JsonUtil.toJsonString(objectNode);
	}
	
	/**
	 * 在json字符串中获取key的value（存使用append2Json）
	 * 
	 * @param jsonString
	 * @param key
	 * @return
	 */
	public static String getByJson(String jsonString, String key) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		ObjectNode objectNode = JsonUtil.toNotNullObjectNode(jsonString);
		JsonNode valueNode = objectNode.get(key);
		if (valueNode == null) {
			return null;
		}
		return valueNode.asText();
	}

	/**
	 * 任意1个匹配predicate则返回,否则返回successResult
	 * 
	 * @param supplierList
	 * @param predicate
	 * @param successResult
	 * @return
	 */
	public static <T> T anyMatch(List<Supplier<T>> supplierList, Predicate<T> predicate, T successResult) {
		// 构建CompletableFuture
		List<CompletableFuture<T>> completableFutureList = supplierList.stream()
				.map(v -> CompletableFuture.supplyAsync(v)).collect(Collectors.toList());

		CompletableFuture<T> result = new CompletableFuture<>();

		CompletableFuture.allOf(completableFutureList.stream().map(f -> f.thenAccept(v -> {
			if (predicate.test(v))
				result.complete(v);
		})).toArray(CompletableFuture<?>[]::new)).whenComplete((ignored, t) -> result.complete(successResult));

		return result.join();
	}
	
	/**
	 * 替换配置的参数
	 * 
	 * @param source
	 * @param configParams
	 * @return
	 */
	public static String replaceConfigParams(String source, Map<String, String> configParams) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		Set<Entry<String, String>> entrySet = configParams.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String value = entry.getValue();
			source = source.replace(entry.getKey(), Optional.ofNullable(value).orElse(""));
		}
		return source;
	}
}
