package com.company.admin.jackson.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.company.admin.jackson.annotation.AutoDesc;
import com.company.admin.util.DescriptionUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * @author xxw
 * @date 2018/8/27
 */
public class AutoDescJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private String propertyName;

    private Map<String, String> dictionary;

    private static final String PROPERTY_NAME_SUFFIX = "Desc";

    public AutoDescJsonSerializer() {}

    private AutoDescJsonSerializer(String propertyName, Map<String, String> dictionary) {
        this.propertyName = propertyName;
        this.dictionary = dictionary;
    }

    private AutoDescJsonSerializer build(String propertyName, String[] values) {
        propertyName = propertyName + PROPERTY_NAME_SUFFIX;
        Map<String, String> dictionary = new HashMap<>(16);
        for (int i = 0; i < values.length; ++i) {
            String[] map = values[i].split(DescriptionUtils.DELIMITER);
            dictionary.put(map[0], map[1]);
        }
        return new AutoDescJsonSerializer(propertyName, dictionary);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeObject(o);
        String currentValue = String.valueOf(o);
        String description = dictionary.get(currentValue);
        if (description != null) {
            jsonGenerator.writeObjectField(propertyName, description);
        }
    }

    @Override
    public JsonSerializer<Object> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
            throws JsonMappingException {
        String propertyName = beanProperty.getName();
        AutoDesc dictionary = beanProperty.getAnnotation(AutoDesc.class);
        String[] value = dictionary.value();
        return build(propertyName, value);
    }
}
