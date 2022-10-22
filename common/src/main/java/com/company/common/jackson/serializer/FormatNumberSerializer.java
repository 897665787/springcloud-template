package com.company.common.jackson.serializer;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import com.company.common.jackson.annotation.FormatNumber;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * 将数值格式化
 * 
 * @author JQ棣
 *
 */
public class FormatNumberSerializer extends JsonSerializer<Number> implements ContextualSerializer {

	private String pattern;

	public FormatNumberSerializer() {
	}

	public FormatNumberSerializer(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void serialize(Number o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		if (StringUtils.isBlank(pattern)) {
			jsonGenerator.writeObject(o);
		} else {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			jsonGenerator.writeObject(decimalFormat.format(o));
		}
	}

	@Override
	public JsonSerializer<Number> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		return new FormatNumberSerializer(beanProperty.getAnnotation(FormatNumber.class).pattern());
	}
}
