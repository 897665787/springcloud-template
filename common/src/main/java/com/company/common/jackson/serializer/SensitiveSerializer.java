package com.company.common.jackson.serializer;

import java.io.IOException;

import com.company.common.enums.DesensitizedType;
import com.company.common.jackson.annotation.Sensitive;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * 数据脱敏
 * 
 * @author JQ棣
 */
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
	private DesensitizedType desensitizedType;

	public SensitiveSerializer() {
	}

	public SensitiveSerializer(DesensitizedType desensitizedType) {
		this.desensitizedType = desensitizedType;
	}

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String desensitized = desensitizedType.desensitizer().apply(value);
		gen.writeString(desensitized);
	}

	@Override
	public JsonSerializer<String> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		return new SensitiveSerializer(beanProperty.getAnnotation(Sensitive.class).value());
	}
}
