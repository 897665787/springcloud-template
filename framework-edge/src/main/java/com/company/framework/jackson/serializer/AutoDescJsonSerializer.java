package com.company.framework.jackson.serializer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.company.framework.jackson.annotation.AutoDesc;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoDescJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

	private String propertyName;

	private Map<String, String> dictionary;

	private static final String PROPERTY_NAME_SUFFIX = "Desc";

	public AutoDescJsonSerializer() {
	}

	private AutoDescJsonSerializer(String propertyName, Map<String, String> dictionary) {
		this.propertyName = propertyName;
		this.dictionary = dictionary;
	}

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeObject(value);
		String currentValue = String.valueOf(value);
		String description = dictionary.get(currentValue);
		if (description != null) {
			gen.writeObjectField(propertyName, description);
		} else {
			log.warn("{} auto desc fail,value:{}", propertyName, value);
		}
	}

	@Override
	public JsonSerializer<Object> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		String propertyName = beanProperty.getName();
		AutoDesc autoDesc = beanProperty.getAnnotation(AutoDesc.class);
		Class<?> clazz = autoDesc.value();
		Enum<?>[] enumList = null;
		try {
			Method method = clazz.getMethod("values");
			Object enumArray = method.invoke(null);
			enumList = (Enum<?>[]) enumArray;
		} catch (Exception e) {
			log.error("AutoDesc enum {} error", clazz.getName(), e);
		}
		List<CodeDesc> codeDescList = Optional.ofNullable(enumList).map(v -> Arrays.asList(v))
				.orElse(Collections.emptyList()).stream()
				.map(enumObj -> codeDesc(enumObj, autoDesc.code(), autoDesc.desc())).filter(Objects::nonNull)
				.collect(Collectors.toList());
		return build(propertyName, codeDescList);
	}

	private AutoDescJsonSerializer build(String propertyName, List<CodeDesc> codeDescList) {
		propertyName = propertyName + PROPERTY_NAME_SUFFIX;
		Map<String, String> dictionary = codeDescList.stream().filter(v -> v.getCode() != null && v.getDesc() != null)
				.collect(Collectors.toMap(CodeDesc::getCode, CodeDesc::getDesc, (a, b) -> b));
		return new AutoDescJsonSerializer(propertyName, dictionary);
	}

	private CodeDesc codeDesc(Object enumObj, String codeField, String descField) {
		String code = getFieldValue(enumObj, codeField);
		if (code == null) {
			return null;
		}
		String desc = getFieldValue(enumObj, descField);
		if (desc == null) {
			return null;
		}
		return new CodeDesc().setCode(code).setDesc(desc);
	}

	private String getFieldValue(Object enumObj, String field) {
		Class<? extends Object> enumClass = enumObj.getClass();
		String getMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
		Object value = null;
		try {
			Method getMethod = enumClass.getMethod(getMethodName);
			value = getMethod.invoke(enumObj);
		} catch (NoSuchMethodException e) {
			log.error("getMethodName：{}", getMethodName, e);
		} catch (Exception e) {
			log.error("getFieldValue error", e);
		}
		return Optional.ofNullable(value).map(Object::toString).orElse(null);
	}

	@Data
	@Accessors(chain = true)
	private class CodeDesc {
		private String code;
		private String desc;
	}
}
