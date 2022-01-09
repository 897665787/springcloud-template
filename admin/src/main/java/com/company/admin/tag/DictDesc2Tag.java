package com.company.admin.tag;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.enums.EnumUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.company.admin.entity.Order;
import com.company.common.jackson.annotation.AutoDesc;
import com.google.common.collect.Maps;

/**
 * 根据value查询desc的标签逻辑
 */
public class DictDesc2Tag extends GeneralVarTagBinding {

	public static void main(String[] args) {
		Class<?> clazz = Order.class;
		String property = "type";
		Field field = ReflectionUtils.findField(clazz, property);
		System.out.println(field);
		
		AutoDesc autoDesc = AnnotationUtils.getAnnotation(field, AutoDesc.class);
		System.out.println(autoDesc);
//		System.out.println(autoDesc.code());
//		System.out.println(autoDesc.desc());
		
		Class<? extends Enum<?>> value2 = autoDesc.value();
		Enum<?>[] enumConstants = value2.getEnumConstants();
		
		String code = autoDesc.code();
		String codeUpper = code.substring(0, 1).toUpperCase() + code.substring(1);
		String desc = autoDesc.desc();
		String descUpper = desc.substring(0, 1).toUpperCase() + desc.substring(1);
		
		Method codeMethod = ReflectionUtils.findMethod(value2, "get" + codeUpper);
		Method descMethod = ReflectionUtils.findMethod(value2, "get" + descUpper);
		
		Map<String, String> descriptions = new LinkedHashMap<>(16);
		
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
	}
	
	Map<String, String> kv = Maps.newHashMap();
	{
		kv.put("a", "a1");
		kv.put("b", "b1");
		kv.put("c", "c1");
		kv.put("d", "d1");
		kv.put("e", "e1");
	}

	@Override
	public void render() {
		StringBuilder html = new StringBuilder();
		
		String htmlTagName = this.getHtmlTagName();
		Map<String, Object> attributes = this.getAttributes();
		Object attributeValue = this.getAttributeValue("key");
		String desc = kv.get(attributeValue);
		html.append("<div class=\"form-group\">");
		html.append(desc);
		html.append("<div>\r\n");
//		BeetlKit.render(template, paras)
		try {
			this.ctx.byteWriter.writeString(html.toString());
		} catch (IOException e) {
			throw new RuntimeException("输出字典标签错误");
		}
	}
}
