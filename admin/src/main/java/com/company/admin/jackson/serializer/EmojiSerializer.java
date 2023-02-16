package com.company.admin.jackson.serializer;

import java.io.IOException;

import com.company.admin.util.EmojiCharacterUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * Emoji序列化工具 <br>
 * Created by JQ棣 on 2018/06/13.
 */
public class EmojiSerializer extends JsonSerializer<String> implements ContextualSerializer {

	@Override
	public void serialize(String o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(EmojiCharacterUtil.reverse(o));
	}

	@Override
	public JsonSerializer<String> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		return new EmojiSerializer();
	}
}