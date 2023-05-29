package com.company.tool.sms;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.tool.entity.SmsTask;
import com.company.tool.enums.SmsEnum;
import com.company.tool.enums.SmsTaskEnum;
import com.company.tool.rabbitmq.Constants;
import com.company.tool.rabbitmq.consumer.strategy.StrategyConstants;
import com.company.tool.rabbitmq.dto.SendSmsMQDto;
import com.company.tool.service.SmsTaskService;

/**
 * 短信发送器（异步）
 */
@Component
public class AsyncSmsSender {

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private SmsTaskService smsTaskService;

	public void send(String mobile, SmsEnum.Type type, Map<String, String> templateParamMap) {
		LocalDateTime overTime = LocalDateTime.now().plusDays(1);
		send(mobile, type, templateParamMap, LocalDateTime.now(), overTime);
	}

	public void send(String mobile, SmsEnum.Type type, Map<String, String> templateParamMap, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		SmsTask smsTask = new SmsTask();
		smsTask.setMobile(mobile);
		smsTask.setType(type.getCode());
		smsTask.setTemplateParamJson(JsonUtil.toJsonString(templateParamMap));
		smsTask.setPlanSendTime(planSendTime);
		smsTask.setOverTime(overTime);

		boolean sendNow = planSendTime.compareTo(LocalDateTime.now()) <= 0;
		SmsTaskEnum.Status status = sendNow ? SmsTaskEnum.Status.PRE_SEND : SmsTaskEnum.Status.PRE_TIME_SEND;
		smsTask.setStatus(status.getCode());
		smsTaskService.insert(smsTask);

		if (sendNow) {
			asyncExe(smsTask.getId(), status);
		}
	}

	/* 定时任务轮询 */
	public List<Integer> select4PreTimeSend() {
		List<Integer> smsTaskIdList = smsTaskService.select4PreTimeSend(SmsTaskEnum.Status.PRE_TIME_SEND);
		return smsTaskIdList;
	}

	public void exePreTimeSend(Integer id) {
		asyncExe(id, SmsTaskEnum.Status.PRE_TIME_SEND);
	}
	/* 定时任务轮询 */

	private void asyncExe(Integer smsTaskId, SmsTaskEnum.Status status) {
		// MQ异步处理
		SendSmsMQDto params = new SendSmsMQDto();
		params.setSmsTaskId(smsTaskId);

		messageSender.sendNormalMessage(StrategyConstants.SENDSMS_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.SEND_SMS.ROUTING_KEY);

		// 必须加状态条件，消费者代码可能会比下面的代码先执行
		smsTaskService.updateStatusByStatus(SmsTaskEnum.Status.SEND_MQ_SUCCESS, smsTaskId, status);
	}
}
