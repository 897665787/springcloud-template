package com.company.admin.tag;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.company.common.jackson.annotation.AutoDesc;

public class DescriptionUtils {

    public static Map<String, String> descriptions(Class<?> clazz, String property) {
		Field field = ReflectionUtils.findField(clazz, property);
		AutoDesc autoDesc = AnnotationUtils.getAnnotation(field, AutoDesc.class);
		
		Class<? extends Enum<?>> value2 = autoDesc.value();
		String code = autoDesc.code();
		String codeUpper = code.substring(0, 1).toUpperCase() + code.substring(1);
		String desc = autoDesc.desc();
		String descUpper = desc.substring(0, 1).toUpperCase() + desc.substring(1);
		
		Method codeMethod = ReflectionUtils.findMethod(value2, "get" + codeUpper);
		Method descMethod = ReflectionUtils.findMethod(value2, "get" + descUpper);
		
		Map<String, String> descriptions = new LinkedHashMap<>();
		
		Enum<?>[] enumConstants = value2.getEnumConstants();
		for (Enum<?> enum1 : enumConstants) {
			try {
				Object codeVal = codeMethod.invoke(enum1);
				Object descVal = descMethod.invoke(enum1);
				System.out.println("code:" + codeVal + " desc:" + descVal);
				descriptions.put(codeVal.toString(), descVal.toString());
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
		}
        return descriptions;
    }
}
