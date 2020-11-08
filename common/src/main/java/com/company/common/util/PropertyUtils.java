package com.company.common.util;

import java.util.Collection;
import java.util.List;

/**
 * 属性工具类
 */
public class PropertyUtils {
	/**
	 * 复制对象属性
	 * 
	 * @param srcObj
	 * @param targetClass
	 * @return
	 */
	public static <E> E copyProperties(Object srcObj, Class<E> targetClass) {
		if(srcObj == null){
			return null;
		}
//		return JSON.parseObject(JSON.toJSONString(srcObj), targetClass);
		return JsonUtil.toEntity(JsonUtil.toJsonString(srcObj), targetClass);
	}

	/**
	 * 复制数组对象属性
	 * 
	 * @param srcArray
	 * @param targetClass
	 * @return
	 */
	public static <E> List<E> copyArrayProperties(Collection<?> srcArray, Class<E> targetClass) {
		if(srcArray == null){
			return null;
		}
//		return JSON.parseArray(JSON.toJSONString(srcArray), targetClass);
		return JsonUtil.toList(JsonUtil.toJsonString(srcArray), targetClass);
	}
}
