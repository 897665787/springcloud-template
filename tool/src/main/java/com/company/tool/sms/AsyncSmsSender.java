package com.company.tool.sms;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.framework.amqp.MessageSender;
import com.company.tool.amqp.Constants;
import com.company.tool.amqp.strategy.StrategyConstants;
import com.company.tool.amqp.strategy.dto.SendSmsMQDto;
import com.company.tool.api.enums.SmsEnum;
import com.company.tool.entity.SmsTask;
import com.company.tool.entity.SmsTaskDetail;
import com.company.tool.enums.SmsTaskDetailEnum;
import com.company.tool.service.SmsTaskDetailService;
import com.company.tool.service.SmsTaskService;
import com.company.tool.sms.dto.MobileTemplateParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;

/**
 * 短信发送器（异步）
 */
@Component
public class AsyncSmsSender {

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private SmsTaskService smsTaskService;
	@Autowired
	private SmsTaskDetailService smsTaskDetailService;

	public void send0(List<String> mobileList, SmsEnum.Type type) {
		Map<String, String> emptyMap = Maps.newHashMap();
		List<MobileTemplateParam> mobileTemplateParamList = mobileList.stream().map(mobile -> {
			MobileTemplateParam mobileTemplateParam = new MobileTemplateParam();
			mobileTemplateParam.setMobile(mobile);
			mobileTemplateParam.setTemplateParamMap(emptyMap);
			return mobileTemplateParam;
		}).collect(Collectors.toList());
		send(mobileTemplateParamList, type);
	}

	public void send(List<MobileTemplateParam> mobileTemplateParamList, SmsEnum.Type type) {
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusDays(1);
		send(mobileTemplateParamList, type, planSendTime, overTime);
	}

	public void send(String mobile, SmsEnum.Type type) {
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusDays(1);
		send(mobile, Maps.newHashMap(), type, planSendTime, overTime);
	}

	public void send(String mobile, SmsEnum.Type type, LocalDateTime planSendTime, LocalDateTime overTime) {
		send(mobile, Maps.newHashMap(), type, planSendTime, overTime);
	}

	public void send(String mobile, Map<String, String> templateParamMap, SmsEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		MobileTemplateParam mobileTemplateParam = new MobileTemplateParam();
		mobileTemplateParam.setMobile(mobile);
		mobileTemplateParam.setTemplateParamMap(templateParamMap);
		send(mobileTemplateParam, type, planSendTime, overTime);
	}

	public void send(MobileTemplateParam mobileTemplateParam, SmsEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		send(Lists.newArrayList(mobileTemplateParam), type, planSendTime, overTime);
	}

	public void send(List<MobileTemplateParam> mobileTemplateParamList, SmsEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {

		SmsTask smsTask = new SmsTask();
		smsTask.setType(type.getCode());
		smsTask.setPlanSendTime(planSendTime);
		smsTask.setOverTime(overTime);
		smsTask.setRemark("AsyncSmsSender-" + DateUtil.format(new Date(), "yyMMddHHmmss"));
		smsTaskService.save(smsTask);

		for (MobileTemplateParam mobileTemplateParam : mobileTemplateParamList) {
			SmsTaskDetail smsTaskDetail = new SmsTaskDetail();
			smsTaskDetail.setTaskId(smsTask.getId());
			smsTaskDetail.setMobile(mobileTemplateParam.getMobile());
			smsTaskDetail.setTemplateParamJson(JsonUtil.toJsonString(mobileTemplateParam.getTemplateParamMap()));
			smsTaskDetail.setPlanSendTime(planSendTime);
			boolean sendNow = planSendTime.compareTo(LocalDateTime.now()) <= 0;
			SmsTaskDetailEnum.Status status = sendNow ? SmsTaskDetailEnum.Status.PRE_SEND
					: SmsTaskDetailEnum.Status.PRE_TIME_SEND;
			smsTaskDetail.setStatus(status.getCode());
			smsTaskDetailService.save(smsTaskDetail);

			if (sendNow) {
				asyncExe(smsTaskDetail.getId(), status);
			}
		}
	}

	/* 定时任务轮询 */
	public List<Integer> select4PreTimeSend(int limit) {
		List<Integer> smsTaskIdList = smsTaskDetailService.select4PreTimeSend(SmsTaskDetailEnum.Status.PRE_TIME_SEND,
				limit);
		return smsTaskIdList;
	}

	public void exePreTimeSend(Integer id) {
		asyncExe(id, SmsTaskDetailEnum.Status.PRE_TIME_SEND);
	}
	/* 定时任务轮询 */

	private void asyncExe(Integer smsTaskDetailId, SmsTaskDetailEnum.Status status) {
		// MQ异步处理
		SendSmsMQDto params = new SendSmsMQDto();
		params.setSmsTaskDetailId(smsTaskDetailId);

		messageSender.sendNormalMessage(StrategyConstants.SENDSMS_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.SEND_SMS.ROUTING_KEY);

		// 必须加状态条件，消费者代码可能会比下面的代码先执行
		SmsTaskDetail smsTaskDetail = smsTaskDetailService.getById(smsTaskDetailId);
		String remark = Utils.rightRemark(smsTaskDetail.getRemark(),
				SmsTaskDetailEnum.Status.SEND_MQ_SUCCESS.getDesc());
		smsTaskDetailService.updateStatusByStatus(SmsTaskDetailEnum.Status.SEND_MQ_SUCCESS, remark, smsTaskDetailId,
				status);
	}
}
