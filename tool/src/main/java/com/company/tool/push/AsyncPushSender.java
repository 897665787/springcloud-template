package com.company.tool.push;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.framework.messagedriven.MessageSender;
import com.company.tool.messagedriven.Constants;
import com.company.tool.messagedriven.strategy.StrategyConstants;
import com.company.tool.messagedriven.strategy.dto.SendPushMQDto;
import com.company.tool.api.enums.PushEnum;
import com.company.tool.entity.PushTask;
import com.company.tool.entity.PushTaskDetail;
import com.company.tool.enums.PushTaskDetailEnum;
import com.company.tool.service.PushTaskDetailService;
import com.company.tool.service.PushTaskService;
import com.company.tool.push.dto.DeviceTemplateParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;

/**
 * 短信发送器（异步）
 */
@Component
public class AsyncPushSender {

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private PushTaskService pushTaskService;
	@Autowired
	private PushTaskDetailService pushTaskDetailService;

	public void send0(List<String> deviceidList, PushEnum.Type type) {
		Map<String, String> emptyMap = Maps.newHashMap();
		List<DeviceTemplateParam> deviceidTemplateParamList = deviceidList.stream().map(deviceid -> {
			DeviceTemplateParam deviceidTemplateParam = new DeviceTemplateParam();
			deviceidTemplateParam.setDeviceid(deviceid);
			deviceidTemplateParam.setTemplateParamMap(emptyMap);
			return deviceidTemplateParam;
		}).collect(Collectors.toList());
		send(deviceidTemplateParamList, type);
	}

	public void send(List<DeviceTemplateParam> deviceidTemplateParamList, PushEnum.Type type) {
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusDays(1);
		send(deviceidTemplateParamList, type, planSendTime, overTime);
	}

	public void send(String deviceid, PushEnum.Type type) {
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusDays(1);
		send(deviceid, Maps.newHashMap(), type, planSendTime, overTime);
	}

	public void send(String deviceid, PushEnum.Type type, LocalDateTime planSendTime, LocalDateTime overTime) {
		send(deviceid, Maps.newHashMap(), type, planSendTime, overTime);
	}

	public void send(String deviceid, Map<String, String> templateParamMap, PushEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		DeviceTemplateParam deviceidTemplateParam = new DeviceTemplateParam();
		deviceidTemplateParam.setDeviceid(deviceid);
		deviceidTemplateParam.setTemplateParamMap(templateParamMap);
		send(deviceidTemplateParam, type, planSendTime, overTime);
	}

	public void send(DeviceTemplateParam deviceidTemplateParam, PushEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		send(Lists.newArrayList(deviceidTemplateParam), type, planSendTime, overTime);
	}

	public void send(List<DeviceTemplateParam> deviceidTemplateParamList, PushEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {

		PushTask pushTask = new PushTask();
		pushTask.setType(type.getCode());
		pushTask.setPlanSendTime(planSendTime);
		pushTask.setOverTime(overTime);
		pushTask.setRemark("AsyncPushSender-" + DateUtil.format(new Date(), "yyMMddHHmmss"));
		pushTaskService.save(pushTask);

		for (DeviceTemplateParam deviceidTemplateParam : deviceidTemplateParamList) {
			PushTaskDetail pushTaskDetail = new PushTaskDetail();
			pushTaskDetail.setTaskId(pushTask.getId());
			pushTaskDetail.setDeviceid(deviceidTemplateParam.getDeviceid());
			pushTaskDetail.setTemplateParamJson(JsonUtil.toJsonString(deviceidTemplateParam.getTemplateParamMap()));
			pushTaskDetail.setPlanSendTime(planSendTime);
			boolean sendNow = planSendTime.compareTo(LocalDateTime.now()) <= 0;
			PushTaskDetailEnum.Status status = sendNow ? PushTaskDetailEnum.Status.PRE_SEND
					: PushTaskDetailEnum.Status.PRE_TIME_SEND;
			pushTaskDetail.setStatus(status.getCode());
			pushTaskDetailService.save(pushTaskDetail);

			if (sendNow) {
				asyncExe(pushTaskDetail.getId(), status);
			}
		}
	}

	/* 定时任务轮询 */
	public List<Integer> select4PreTimeSend(int limit) {
		List<Integer> pushTaskIdList = pushTaskDetailService.select4PreTimeSend(PushTaskDetailEnum.Status.PRE_TIME_SEND,
				limit);
		return pushTaskIdList;
	}

	public void exePreTimeSend(Integer id) {
		asyncExe(id, PushTaskDetailEnum.Status.PRE_TIME_SEND);
	}
	/* 定时任务轮询 */

	private void asyncExe(Integer pushTaskDetailId, PushTaskDetailEnum.Status status) {
		// MQ异步处理
		SendPushMQDto params = new SendPushMQDto();
		params.setPushTaskDetailId(pushTaskDetailId);

		messageSender.sendNormalMessage(StrategyConstants.SENDSMS_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.SEND_SMS.KEY);

		// 必须加状态条件，消费者代码可能会比下面的代码先执行
		PushTaskDetail pushTaskDetail = pushTaskDetailService.getById(pushTaskDetailId);
		String remark = Utils.rightRemark(pushTaskDetail.getRemark(),
				PushTaskDetailEnum.Status.SEND_MQ_SUCCESS.getDesc());
		pushTaskDetailService.updateStatusByStatus(PushTaskDetailEnum.Status.SEND_MQ_SUCCESS, remark, pushTaskDetailId,
				status);
	}
}
