package com.company.order.innercallback.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.common.util.Utils;
import com.company.framework.context.SpringContextUtil;
import com.company.order.entity.InnerCallback;
import com.company.order.enums.InnerCallbackEnum;
import com.company.order.innercallback.config.InnercallbackConfig;
import com.company.order.innercallback.header.HeaderName;
import com.company.order.innercallback.processor.AbandonRequestProcessor;
import com.company.order.innercallback.processor.BeforeRequestProcessor;
import com.company.order.innercallback.processor.ReturnSuccessProcessor;
import com.company.order.innercallback.processor.bean.ProcessorBeanName;
import com.company.order.innercallback.strategy.SecondsStrategyBeanFactory;
import com.company.order.mapper.InnerCallbackMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InnerCallbackServiceImpl extends ServiceImpl<InnerCallbackMapper, InnerCallback>
		implements IInnerCallbackService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ThreadPoolTaskExecutor executor;
	@Autowired
	private InnercallbackConfig innercallbackConfig;

	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams) {
		return postRestTemplate(notifyUrl, jsonParams, null);
	}

	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams, int maxFailure) {
		return postRestTemplate(notifyUrl, jsonParams, null, innercallbackConfig.getDefaultIncreaseSeconds(), maxFailure, InnerCallbackEnum.SecondsStrategy.INCREMENT);
	}

	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams, int increaseSeconds, int maxFailure) {
		return postRestTemplate(notifyUrl, jsonParams, null, increaseSeconds, maxFailure, InnerCallbackEnum.SecondsStrategy.INCREMENT);
	}

	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams, int increaseSeconds, int maxFailure, InnerCallbackEnum.SecondsStrategy secondsStrategy) {
		return postRestTemplate(notifyUrl, jsonParams, null, increaseSeconds, maxFailure, secondsStrategy);
	}

	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName) {
		return postRestTemplate(notifyUrl, jsonParams, processorBeanName, innercallbackConfig.getDefaultIncreaseSeconds(), innercallbackConfig.getDefaultMaxfailure(), InnerCallbackEnum.SecondsStrategy.INCREMENT);
	}

	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName,
			int maxFailure) {
		return postRestTemplate(notifyUrl, jsonParams, processorBeanName, innercallbackConfig.getDefaultIncreaseSeconds(), maxFailure, InnerCallbackEnum.SecondsStrategy.INCREMENT);
	}
	
	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName,
			int increaseSeconds, int maxFailure, InnerCallbackEnum.SecondsStrategy secondsStrategy) {
		return postRestTemplate(notifyUrl, jsonParams, processorBeanName, increaseSeconds, maxFailure, secondsStrategy, new Date());
	}

	@Override
	public Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName,
			int increaseSeconds, int maxFailure, InnerCallbackEnum.SecondsStrategy secondsStrategy, Date nextDisposeTime) {
		InnerCallback innerCallback = new InnerCallback().setUrl(notifyUrl).setJsonParams(JsonUtil.toJsonString(jsonParams))
				.setProcessorBeanName(processorBeanName == null ? null : JsonUtil.toJsonString(processorBeanName))
				.setStatus(InnerCallbackEnum.Status.PRE_CALLBACK.getCode())
				.setIncreaseSeconds(increaseSeconds)
				.setNextDisposeTime(nextDisposeTime).setMaxFailure(maxFailure)
				.setFailure(0).setTraceId(MdcUtil.get())
				.setSecondsStrategy(secondsStrategy.getCode());
		baseMapper.insert(innerCallback);
		
		if (nextDisposeTime.after(new Date())) {
			// 如果传入的nextDisposeTime在当前时间之后，说明需要延迟一段时间才开始执行，把状态修改为CALLBACK_FAIL利用selectId4CallbackFail来跑
			InnerCallback innerCallback4Update = new InnerCallback().setId(innerCallback.getId())
					.setStatus(InnerCallbackEnum.Status.CALLBACK_FAIL.getCode());
			baseMapper.updateById(innerCallback4Update);
			return true;
		}
		return postRestTemplateAsync(innerCallback);
	}

	@Override
	public Boolean postRestTemplate(Integer id) {
		InnerCallback innerCallback = baseMapper.selectById(id);
		Optional.ofNullable(innerCallback.getTraceId()).ifPresent(MdcUtil::put);// 重试情况下使用同一个logid方便追踪日志
		InnerCallback innerCallback4Update = new InnerCallback();
		innerCallback4Update.setId(id);
		innerCallback4Update.setStatus(InnerCallbackEnum.Status.PRE_CALLBACK.getCode());
		baseMapper.updateById(innerCallback4Update);
		return postRestTemplateAsync(innerCallback);
	}

	private Boolean postRestTemplateAsync(InnerCallback innerCallback) {
		executor.submit(() -> postRestTemplate(innerCallback));
		return true;
	}
	
	private Boolean postRestTemplate(InnerCallback innerCallback) {
		String jsonParams = innerCallback.getJsonParams();
		ProcessorBeanName processorBeanName = Optional.ofNullable(innerCallback.getProcessorBeanName())
				.map(v -> JsonUtil.toEntity(v, ProcessorBeanName.class)).orElse(new ProcessorBeanName());
		String beforeRequest = processorBeanName.getBeforeRequest();
		Object paramObject = null;
		if (beforeRequest != null) {
			paramObject = SpringContextUtil.getBean(beforeRequest, BeforeRequestProcessor.class)
					.beforeRequest(jsonParams);
		} else {
			paramObject = JsonUtil.readTree(jsonParams);
		}
		String remark = null;
		boolean abandonCallback = false;
		log.info("回调,请求地址:{},原参数:{},参数:{}", innerCallback.getUrl(), jsonParams, JsonUtil.toJsonString(paramObject));
		long start = System.currentTimeMillis();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HeaderName.FAILURE, String.valueOf(innerCallback.getFailure()));
			headers.add(HeaderName.MAX_FAILURE, String.valueOf(innerCallback.getMaxFailure()));
			MdcUtil.headers2().forEach((k, v) -> headers.addAll(k, v));// 日志追踪ID
			HttpEntity<Object> httpEntity = new HttpEntity<>(paramObject, headers);
			@SuppressWarnings("rawtypes")
			ResponseEntity<Result> responseEntity = restTemplate.postForEntity(innerCallback.getUrl(), httpEntity,
					Result.class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				@SuppressWarnings("unchecked")
				Result<Boolean> result = responseEntity.getBody();
				log.info("{}ms,回调结果:{}", System.currentTimeMillis() - start, JsonUtil.toJsonString(result));
				remark = result.getMessage();
				if (result.successCode()) {
					remark = Utils.rightRemark(innerCallback.getRemark(), remark);
					
					baseMapper.callbackSuccess(InnerCallbackEnum.Status.CALLBACK_SUCCESS, JsonUtil.toJsonString(result),
							remark, innerCallback.getId());
					Optional.ofNullable(processorBeanName.getReturnSuccess())
							.map(v -> SpringContextUtil.getBean(v, ReturnSuccessProcessor.class))
							.ifPresent(v -> v.afterReturnSuccess(jsonParams));
					return true;
				} else {
					Boolean retry = result.getData();
					abandonCallback = Optional.ofNullable(retry).orElse(abandonCallback);
				}
			} else {
				remark = "响应码:" + responseEntity.getStatusCodeValue();
			}
		} catch (Exception e) {
			log.error("{}ms,回调异常", System.currentTimeMillis() - start, e);
			remark = ExceptionUtils.getMessage(e);
		}
		String abandonReason = remark;
		remark = Utils.rightRemark(innerCallback.getRemark(), remark);
		
		String secondsStrategy = innerCallback.getSecondsStrategy();
		int nextSeconds = SecondsStrategyBeanFactory.of(secondsStrategy).nextSeconds(innerCallback.getIncreaseSeconds(),
				innerCallback.getFailure());
		Date nextDisposeTime = DateUtils.addSeconds(innerCallback.getNextDisposeTime(), nextSeconds);
		baseMapper.callbackFail(InnerCallbackEnum.Status.CALLBACK_FAIL, nextDisposeTime,
				remark, innerCallback.getId());
		if (abandonCallback || innerCallback.getFailure() + 1 >= innerCallback.getMaxFailure()) {
			baseMapper.abandonCallback(InnerCallbackEnum.Status.ABANDON_CALLBACK, innerCallback.getId());
			Optional.ofNullable(processorBeanName.getAbandonRequest())
					.map(v -> SpringContextUtil.getBean(v, AbandonRequestProcessor.class))
					.ifPresent(v -> v.afterAbandonRequest(jsonParams, abandonReason));
		}
		return false;
	}

	@Override
	public List<Integer> selectId4CallbackFail() {
		return baseMapper.selectId4CallbackFail(InnerCallbackEnum.Status.CALLBACK_FAIL.getCode());
	}
	
	public static void main(String[] args) {
		// 等比数列求和公式
		double a1 = 60;// 基数
		int retrytimes = 13;// 重试次数
		
		double q = 2;// 比
		int n = retrytimes - 1;// 项数

		for (int i = 1; i <= n; i++) {
			System.out.println("第" + (i + 1) + "次等待秒数：" + (a1 * Math.pow(q, i - 1)));
		}

		double sn = a1 * (1.0 - Math.pow(q, n)) / (1 - q);

		System.out.println("秒数:" + sn);
		System.out.println("分钟:" + sn / 60);
		System.out.println("小时:" + sn / 60 / 60);
		System.out.println("天数:" + sn / 60 / 60 / 24);
	}

}
