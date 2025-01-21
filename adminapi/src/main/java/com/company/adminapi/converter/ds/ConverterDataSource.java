package com.company.adminapi.converter.ds;

import java.util.Map;
import java.util.Set;

public interface ConverterDataSource {
	Map<Object, String> getFieldConverterValue(Set<Object> fieldValueSet);
}