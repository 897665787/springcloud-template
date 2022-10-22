package com.company.admin.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.company.admin.jackson.annotation.AutoDesc;

/**
 * @author xxw
 * @date 2018/8/27
 */
public class DescriptionUtils {

    public static final String DELIMITER = ":";

    public static String description(Class<?> clazz, String propertyName, Object value) {
        Field field;
        try {
            field = clazz.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        String valueStr = String.valueOf(value);
        AutoDesc dictionary = field.getAnnotation(AutoDesc.class);
        String[] maps = dictionary.value();
        for (String map : maps) {
            boolean describe = map.startsWith(valueStr + DELIMITER);
            if (describe) {
                return map.split(DELIMITER)[1];
            }
        }
        return StringUtils.EMPTY;
    }

    public static Map<String, String> descriptions(Class<?> clazz, String propertyName) {
        Field field;
        try {
            field = clazz.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> descriptions = new HashMap<>(16);
        AutoDesc dictionary = field.getAnnotation(AutoDesc.class);
        String[] maps = dictionary.value();
        for (String map : maps) {
            String[] valueAndDesc = map.split(DELIMITER);
            descriptions.put(valueAndDesc[0], valueAndDesc[1]);
        }
        return descriptions;
    }
}
