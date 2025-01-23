package com.company.adminapi.converter.ds;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

/**
 * 默认数据源，最好不要用
 */
public class DefaultConverterDataSource implements ConverterDataSource {

	@Override
	public Map<Object, String> getFieldConverterValue(Set<Object> fieldValueSet) {
		Map<Object, String> map = Maps.newHashMap();
		for (Object fieldValue : fieldValueSet) {
			map.put(fieldValue, String.valueOf(fieldValue));
		}
		return map;
	}
}