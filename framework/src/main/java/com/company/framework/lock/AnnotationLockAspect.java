package com.company.framework.lock;

import com.company.framework.lock.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 注解Lock切面
 *
 * @author JQ棣
 */
@Slf4j
@Aspect
public class AnnotationLockAspect {
    private final ExpressionParser parser = new SpelExpressionParser();
    private static final String HASH = "#";

    private final LockClient lockClient;

    public AnnotationLockAspect(LockClient lockClient) {
        this.lockClient = lockClient;
    }

    @Around("@annotation(lock)")
    public Object doInLock(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        String key = this.parseKey(lock.value(), parameterNames, args);
        if (StringUtils.isEmpty(key)) {
            return joinPoint.proceed();
        }
        return lockClient.doInLock(key, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String parseKey(String value, String[] parameterNames, Object[] parameterValues) {
        if (!value.contains(HASH)) {
            return value;
        }
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; ++i) {
            evaluationContext.setVariable(parameterNames[i], parameterValues[i]);
        }
        Expression exp = this.parser.parseExpression(value);
        return exp.getValue(evaluationContext, String.class);
    }
}
