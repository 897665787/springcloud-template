package com.company.admin.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.company.common.exception.BusinessException;

import java.util.Set;

/**
 * 模型验证工具
 * Created by JQ棣 on 2018/6/6.
 */
public class ModelValidateUtil {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static Validator getValidator() {
        return validatorFactory.getValidator();
    }

    public static <T> boolean validate(T object, Class<?> rule) {
        Set<ConstraintViolation<T>> constraintViolations = validatorFactory.getValidator().validate(object, rule);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(constraintViolations)) {
            throw new BusinessException(constraintViolations.iterator().next().getMessage());
        }
        return true;
    }
}
