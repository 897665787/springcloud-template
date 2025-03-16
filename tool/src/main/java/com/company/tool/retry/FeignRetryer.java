package com.company.tool.retry;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.company.common.api.Result;
import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.common.util.Utils;
import com.company.tool.api.enums.RetryerEnum;
import com.company.tool.entity.RetryTask;
import com.company.tool.enums.RetryTaskEnum;
import com.company.tool.mapper.RetryTaskMapper;
import com.company.tool.retry.strategy.SecondsStrategyBeanFactory;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FeignRetryer {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ThreadPoolTaskExecutor executor;
	@Autowired
	private RetryTaskMapper baseMapper;

	@Value("${retry.maxDays:30}")
	private Integer maxDays;

	public void call(RetryerInfo retryerInfo) {
		String feignUrl = retryerInfo.getFeignUrl();
		Object jsonParams = retryerInfo.getJsonParams();
		int increaseSeconds = retryerInfo.getIncreaseSeconds();
		int maxFailure = retryerInfo.getMaxFailure();
		RetryerEnum.SecondsStrategy secondsStrategy = retryerInfo.getSecondsStrategy();
		if (secondsStrategy == null) {
			secondsStrategy = RetryerEnum.SecondsStrategy.INCREMENT;
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextDisposeTime = retryerInfo.getNextDisposeTime();
		if (nextDisposeTime == null) {
			nextDisposeTime = now;
		}

		RetryTask retryTask = new RetryTask()
				.setUrl(feignUrl)
				.setJsonParams(JsonUtil.toJsonString(jsonParams))
				.setStatus(RetryTaskEnum.Status.PRE_CALL.getCode())
				.setIncreaseSeconds(increaseSeconds)
				.setNextDisposeTime(nextDisposeTime)
				.setMaxFailure(maxFailure)
				.setFailure(0)
				.setTraceId(MdcUtil.get())
				.setSecondsStrategy(secondsStrategy.getCode());
		baseMapper.insert(retryTask);

		if (nextDisposeTime.isAfter(now)) {
			// 如果传入的nextDisposeTime在当前时间之后，说明需要延迟一段时间才开始执行，把状态修改为CALL_FAIL利用selectId4CallFail来跑
			RetryTask retryTask4Update = new RetryTask().setId(retryTask.getId())
					.setStatus(RetryTaskEnum.Status.CALL_FAIL.getCode());
			baseMapper.updateById(retryTask4Update);
			return;
		}
		callAsync(retryTask);
	}

	public void call(Integer id) {
		RetryTask retryTask = baseMapper.selectById(id);
		Optional.ofNullable(retryTask.getTraceId()).ifPresent(MdcUtil::put);// 重试情况下使用同一个logid方便追踪日志

		// 安全措施，防止很久之前的数据被误调用
		LocalDateTime createTime = retryTask.getCreateTime();
		long days = LocalDateTimeUtil.between(createTime, LocalDateTime.now(), ChronoUnit.DAYS);
		if (days > maxDays) {
			throw new BusinessException(id + "创建" + days + "天，超过" + maxDays + "天，不允许执行");
		}

		RetryTask retryTask4Update = new RetryTask();
		retryTask4Update.setId(id);
		retryTask4Update.setStatus(RetryTaskEnum.Status.PRE_CALL.getCode());
		baseMapper.updateById(retryTask4Update);
		callAsync(retryTask);
	}

	private void callAsync(RetryTask retryTask) {
		executor.submit(() -> call(retryTask));
	}

	private void call(RetryTask retryTask) {
		String jsonParams = retryTask.getJsonParams();
		Object paramObject = JsonUtil.toJsonNode(jsonParams);
		String remark = null;
		boolean abandonCall = false;
		log.info("调用,地址:{},参数:{}", retryTask.getUrl(), jsonParams);
		long start = System.currentTimeMillis();
		try {
			HttpHeaders headers = new HttpHeaders();
			MdcUtil.headers2().forEach((k, v) -> headers.addAll(k, v));// 日志追踪ID
			HttpEntity<Object> httpEntity = new HttpEntity<>(paramObject, headers);
			@SuppressWarnings("rawtypes")
			ResponseEntity<Result> responseEntity = restTemplate.postForEntity(retryTask.getUrl(), httpEntity,
					Result.class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				@SuppressWarnings("unchecked")
				Result<Boolean> result = responseEntity.getBody();
				log.info("{}ms,调用结果:{}", System.currentTimeMillis() - start, JsonUtil.toJsonString(result));
				remark = result.getMessage();
				if (result.successCode()) {
					remark = Utils.rightRemark(retryTask.getRemark(), remark);

					baseMapper.retrySuccess(RetryTaskEnum.Status.CALL_SUCCESS, JsonUtil.toJsonString(result),
							remark, retryTask.getId());
					return;
				} else {
					Boolean stopCall = result.getData();
					if (stopCall != null) {
						abandonCall = stopCall;
					}
				}
			} else {
				remark = "响应码:" + responseEntity.getStatusCodeValue();
			}
		} catch (Exception e) {
			log.error("{}ms,调用异常", System.currentTimeMillis() - start, e);
			remark = ExceptionUtils.getMessage(e);
		}
		remark = Utils.rightRemark(retryTask.getRemark(), remark);

		String secondsStrategy = retryTask.getSecondsStrategy();
		int nextSeconds = SecondsStrategyBeanFactory.of(secondsStrategy).nextSeconds(retryTask.getIncreaseSeconds(),
				retryTask.getFailure());
		LocalDateTime nextDisposeTime = retryTask.getNextDisposeTime().plusSeconds(nextSeconds);
		baseMapper.retryFail(RetryTaskEnum.Status.CALL_FAIL, nextDisposeTime, remark, retryTask.getId());
		if (abandonCall || retryTask.getFailure() + 1 >= retryTask.getMaxFailure()) {
			baseMapper.abandonRetry(RetryTaskEnum.Status.ABANDON_CALL, retryTask.getId());
		}
	}

	public List<Integer> selectId4CallFail() {
		return baseMapper.selectId4CallFail(RetryTaskEnum.Status.CALL_FAIL.getCode());
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
