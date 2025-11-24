package com.company.framework.jackson.serializer;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import com.company.framework.jackson.annotation.FormatNumber;
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
    private RoundingMode roundingMode;

    public FormatNumberSerializer() {
    }

    public FormatNumberSerializer(String pattern, RoundingMode roundingMode) {
        this.pattern = pattern;
        this.roundingMode = roundingMode;
    }

    @Override
    public void serialize(Number o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        if (StringUtils.isBlank(pattern)) {
            jsonGenerator.writeObject(o);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            decimalFormat.setRoundingMode(roundingMode);
            jsonGenerator.writeObject(decimalFormat.format(o));
        }
    }

    @Override
    public JsonSerializer<Number> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
            throws JsonMappingException {
        FormatNumber formatNumber = beanProperty.getAnnotation(FormatNumber.class);
        return new FormatNumberSerializer(formatNumber.pattern(), formatNumber.roundingMode());
    }
}
