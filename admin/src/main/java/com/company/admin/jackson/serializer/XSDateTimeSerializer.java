package com.company.admin.jackson.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.company.admin.jackson.annotation.XSDateTime;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * 时间序列化工具
 * Created by xuxiaowei on 2017/11/14.
 */
public class XSDateTimeSerializer extends JsonSerializer<Date> implements ContextualSerializer {

    private String field;

    private String pattern;

    private String format;

    public XSDateTimeSerializer() {}

    public XSDateTimeSerializer(String field, String pattern, String format) {
        this.field = field;
        this.pattern = pattern;
        this.format = format;
    }

    @Override
    public void serialize(Date o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (StringUtils.isBlank(pattern)) {
            jsonGenerator.writeObject(o);
        }
        else {
            jsonGenerator.writeObject(new SimpleDateFormat(pattern).format(o));
        }
        if (StringUtils.isBlank(format)) {
            if (o instanceof Date) {
                Long time = System.currentTimeMillis() - (o).getTime();
                
                Calendar instance = Calendar.getInstance();
        		instance.setTime(o);
        		
                if (time < 1000 * 60L)
                	jsonGenerator.writeObjectField(field + "Desc", "刚刚");
                else if (time >= 1000 * 60L && time < 1000 * 60 * 60L)
                    jsonGenerator.writeObjectField(field + "Desc", String.valueOf(time / (1000 * 60)) + "分钟前");
                else if (time >= 1000 * 60 * 60L && time < 1000 * 60 * 60 * 24L)
                    jsonGenerator.writeObjectField(field + "Desc", String.valueOf(time / (1000 * 60 * 60)) + "小时前");
                else if (time >= 1000 * 60 * 60 * 24L && time < 1000 * 60 * 60 * 24 * 2L)
                    jsonGenerator.writeObjectField(field + "Desc", "昨天");
                else if (time >= 1000 * 60 * 60 * 24 * 2L && instance.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
                    jsonGenerator.writeObjectField(field + "Desc", new SimpleDateFormat("MM-dd").format(o));
                else
                	jsonGenerator.writeObjectField(field + "Desc", new SimpleDateFormat("yyyy-MM-dd").format(o));
            }
        }
        else {
            jsonGenerator.writeObjectField(field + "Desc", new SimpleDateFormat(format).format(o));
        }
    }

    @Override
    public JsonSerializer<Date> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        return new XSDateTimeSerializer(beanProperty.getName(), beanProperty.getAnnotation(XSDateTime.class).pattern(),
                beanProperty.getAnnotation(XSDateTime.class).format());
    }
}