package com.company.admin.aspect;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.company.admin.jackson.annotation.Emoji;
import com.company.admin.util.EmojiCharacterUtil;

/**
 * Emoji切面<br>
 * Created by JQ棣 on 2018/06/14.
 */
@Service
@Aspect
public class EmojiAspect {
	private static final Logger logger = LoggerFactory.getLogger(EmojiAspect.class);

	@Before("@annotation(emoji)")
	public void doBefore(JoinPoint joinPoint, Emoji emoji) throws Throwable {
		Object[] args = joinPoint.getArgs();

		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method thisMethod = methodSignature.getMethod();

		// 在方法参数中寻找添加Emoji注解的参数
		Annotation[][] annotations = thisMethod.getParameterAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			boolean hasEmojiAnnotation = false;

			Annotation[] annotations2 = annotations[i];
			for (int j = 0; j < annotations2.length; j++) {
				Annotation annotation = annotations2[j];
				if (annotation instanceof Emoji) {
					hasEmojiAnnotation = true;
					break;
				}
			}

			if (!hasEmojiAnnotation) {
				continue;
			}

			dealEmojiField(args[i]);
		}
	}

	private void dealEmojiField(Object arg) {
		Class<?> beanClass = arg.getClass();
		Field[] fields = beanClass.getDeclaredFields();
		// 在对象属性中寻找添加Emoji注解的属性
		for (Field field : fields) {
			boolean hasAnnotation = field.isAnnotationPresent(Emoji.class);
			if (hasAnnotation) {
				PropertyDescriptor pd = null;
				try {
					pd = new PropertyDescriptor(field.getName(), beanClass);
				} catch (IntrospectionException e) {
					logger.error("Introspection Exception", e);
				}
				Method getMethod = pd.getReadMethod();
				Method setMethod = pd.getWriteMethod();
				try {
					Object value = getMethod.invoke(arg);
					if (value != null) {
						setMethod.invoke(arg, EmojiCharacterUtil.escape((String) value));
					}
				} catch (IllegalAccessException e) {
					logger.error("Illegal Access Exception", e);
				} catch (IllegalArgumentException e) {
					logger.error("Illegal Argument Exception", e);
				} catch (InvocationTargetException e) {
					logger.error("Invocation Target Exception", e);
				}
			}
		}
	}
}
