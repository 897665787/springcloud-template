package com.company.adminapi.aspect;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.company.adminapi.amqp.rabbitmq.Constants;
import com.company.adminapi.amqp.strategy.StrategyConstants;
import com.company.adminapi.amqp.strategy.dto.SysOperLogDto;
import com.company.adminapi.annotation.OperationLog;
import com.company.adminapi.enums.OperationLogEnum;
import com.company.framework.amqp.MessageSender;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.util.WebUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {
	/** 排除敏感属性字段 */
	public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };
	
	/** 计算操作消耗时间 */
	private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

	@Autowired
	private MessageSender messageSender;
	
	/**
	 * 处理请求前执行
	 */
	@Before(value = "@annotation(operationLog)")
	public void doBefore(OperationLog operationLog) {
		TIME_THREADLOCAL.set(System.currentTimeMillis());
	}

	/**
	 * 处理完请求后执行
	 *
	 * @param joinPoint 切点
	 */
	@AfterReturning(pointcut = "@annotation(operationLog)", returning = "jsonResult")
	public void doAfterReturning(JoinPoint joinPoint, OperationLog operationLog, Object jsonResult) {
		this.handleLog(joinPoint, operationLog, null, jsonResult);
	}

	/**
	 * 拦截异常操作
	 * 
	 * @param joinPoint 切点
	 * @param e         异常
	 */
	@AfterThrowing(value = "@annotation(operationLog)", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, OperationLog operationLog, Exception e) {
		this.handleLog(joinPoint, operationLog, e, null);
	}

	/**
	 * 封装请求入参和响应结果，并保存到数据库
	 */
	private void handleLog(JoinPoint joinPoint, OperationLog operationLog, Exception exception, Object jsonResult) {
		Integer sysUserId = HttpContextUtil.currentUserIdInt();

		HttpServletRequest request = HttpContextUtil.request();

		SysOperLogDto params = new SysOperLogDto();
		params.setSysUserId(sysUserId);
		params.setOperUrl(StringUtils.left(request.getRequestURI(), 255));
		params.setOperIp(HttpContextUtil.getRequestIp());

		// 设置方法名称
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		params.setMethod(className + "." + methodName + "()");
		// 设置请求方式
		params.setRequestMethod(request.getMethod());

		params.setStatus(OperationLogEnum.BusinessStatus.SUCCESS.ordinal());
		if (exception != null) {
			params.setStatus(OperationLogEnum.BusinessStatus.FAIL.ordinal());
			params.setErrorMsg(StringUtils.left(exception.getMessage(), 2000));
		}

		// 设置action动作
		params.setBusinessType(operationLog.businessType().ordinal());
		// 设置标题
		params.setTitle(operationLog.title());
		// 是否需要保存request，参数和值
		if (operationLog.isSaveRequestData()) {
			String operParamStr = this.getRequestParams(request, joinPoint, params.getRequestMethod(),
					operationLog.excludeParamNames());
			params.setOperParam(operParamStr);
		}
		// 是否需要保存response，参数和值
		if (operationLog.isSaveResponseData() && jsonResult != null) {
			String jsonResultStr = StringUtils.left(JSON.toJSONString(jsonResult), 2000);
			params.setJsonResult(jsonResultStr);
		}
		// 设置消耗时间
		params.setCostTime((int) (System.currentTimeMillis() - TIME_THREADLOCAL.get()));
		params.setOperTime(LocalDateTime.now());

		try {
			// MQ 异步保存
			messageSender.sendNormalMessage(StrategyConstants.SAVE_OPERLOG_STRATEGY, params, Constants.EXCHANGE.DIRECT,
					Constants.QUEUE.COMMON.ROUTING_KEY);
		} catch (Exception e) {
			log.error("异常信息", e);
		} finally {
			TIME_THREADLOCAL.remove();
		}
	}

	/**
	 * 格式化请求参数，封装为 JSON 格式
	 */
	private String getRequestParams(HttpServletRequest request, JoinPoint joinPoint, String requestMethod, String[] excludeParamNames) {
		// 将一键多值的参数转换为一键一值，多个值之间使用英文逗号分隔
		Map<String, String> paramsMap = WebUtil.getReqParam(request);
		HttpMethod httpMethod = HttpMethod.valueOf(requestMethod);

		String paramsStr;
		if (paramsMap.isEmpty() && (HttpMethod.PUT == httpMethod || HttpMethod.POST == httpMethod)) {
			paramsStr = this.argsArrayToString(joinPoint.getArgs(), excludeParamNames);
		} else {
			paramsStr = JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames));
		}
		return StringUtils.left(paramsStr, 2000);
	}

	/**
	 * 接受一个对象数组， 然后将对象数组中的每个对象转换为 JSON 字符串， 排除指定的属性，并将这些 JSON 字符串连接起来，最后返回一个字符串
	 */
	private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
		StringBuilder params = new StringBuilder();
		if (ArrayUtils.isNotEmpty(paramsArray)) {
			for (Object o : paramsArray) {
				if (Objects.nonNull(o) && !this.isFilterObject(o)) {
					try {
						String jsonStr = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
						params.append(jsonStr).append(" ");
					} catch (Exception ignored) {
					}
				}
			}
		}
		return params.toString().trim();
	}

	/**
	 * 忽略敏感属性
	 */
	public PropertyPreFilters.MySimplePropertyPreFilter excludePropertyPreFilter(String[] excludeParamNames) {
		return new PropertyPreFilters().addFilter()
				.addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
	}
	
	/**
	 * <pre>
	 * 检查一个对象是否是 MultipartFile、HttpServletRequest、HttpServletResponse 或 BindingResult类型， 或者是否包含这些类型的元素，如果是则返回 true，表示该对象应该被过滤掉
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	private boolean isFilterObject(final Object o) {
		Class<?> clazz = o.getClass();
		if (clazz.isArray()) {
			return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
		} else if (Collection.class.isAssignableFrom(clazz)) {
			Collection collection = (Collection) o;
			for (Object value : collection) {
				return value instanceof MultipartFile;
			}
		} else if (Map.class.isAssignableFrom(clazz)) {
			Map map = (Map) o;
			for (Object value : map.entrySet()) {
				Map.Entry entry = (Map.Entry) value;
				return entry.getValue() instanceof MultipartFile;
			}
		}
		return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
				|| o instanceof BindingResult;
	}

}
