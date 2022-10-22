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

public class PlusMinusNumberSerializer extends JsonSerializer<Number> implements ContextualSerializer {

	private String pattern;

	public PlusMinusNumberSerializer() {
	}

	public PlusMinusNumberSerializer(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void serialize(Number o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		String val = null;
		if (StringUtils.isNotBlank(pattern)) {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			val = decimalFormat.format(o);
		} else {
			val = String.valueOf(o);
		}
		
		double d = o.doubleValue();
		if (d > 0) {
			val = "+" + val;
		}
		
		jsonGenerator.writeObject(val);
	}

	@Override
	public JsonSerializer<Number> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		return new PlusMinusNumberSerializer(beanProperty.getAnnotation(FormatNumber.class).pattern());
	}
}
