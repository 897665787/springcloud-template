package com.company.tool.subscribe;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.framework.amqp.MessageSender;
import com.company.tool.amqp.Constants;
import com.company.tool.amqp.strategy.StrategyConstants;
import com.company.tool.amqp.strategy.dto.SendSubscribeMQDto;
import com.company.tool.api.enums.SubscribeEnum;
import com.company.tool.entity.SubscribeTask;
import com.company.tool.entity.SubscribeTaskDetail;
import com.company.tool.enums.SubscribeTaskDetailEnum;
import com.company.tool.service.SubscribeTaskDetailService;
import com.company.tool.service.SubscribeTaskService;
import com.company.tool.subscribe.dto.OpenidTemplateParam;
import com.google.common.collect.Lists;

import cn.hutool.core.date.DateUtil;

/**
 * 订阅消息发送器（异步）
 */
@Component
public class AsyncSubscribeSender {

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private SubscribeTaskService subscribeTaskService;
	@Autowired
	private SubscribeTaskDetailService subscribeTaskDetailService;

	public void send(List<OpenidTemplateParam> openidTemplateParamList, SubscribeEnum.Type type) {
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusHours(1);
		send(openidTemplateParamList, type, planSendTime, overTime);
	}

	public void send(String openid, String page, List<String> valueList, SubscribeEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		OpenidTemplateParam openidTemplateParam = new OpenidTemplateParam();
		openidTemplateParam.setOpenid(openid);
		openidTemplateParam.setPage(page);
		openidTemplateParam.setValueList(valueList);
		send(openidTemplateParam, type, planSendTime, overTime);
	}

	public void send(OpenidTemplateParam openidTemplateParam, SubscribeEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		send(Lists.newArrayList(openidTemplateParam), type, planSendTime, overTime);
	}

	public void send(List<OpenidTemplateParam> openidTemplateParamList, SubscribeEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {

		SubscribeTask subscribeTask = new SubscribeTask();
		subscribeTask.setType(type.getCode());
		subscribeTask.setPlanSendTime(planSendTime);
		subscribeTask.setOverTime(overTime);
		subscribeTask.setRemark("AsyncSubscribeSender-" + DateUtil.format(new Date(), "yyMMddHHmmss"));
		subscribeTaskService.save(subscribeTask);

		for (OpenidTemplateParam openidTemplateParam : openidTemplateParamList) {
			SubscribeTaskDetail subscribeTaskDetail = new SubscribeTaskDetail();
			subscribeTaskDetail.setTaskId(subscribeTask.getId());
			subscribeTaskDetail.setOpenid(openidTemplateParam.getOpenid());
			subscribeTaskDetail.setPage(openidTemplateParam.getPage());
			subscribeTaskDetail.setTemplateParamJson(JsonUtil.toJsonString(openidTemplateParam.getValueList()));
			subscribeTaskDetail.setPlanSendTime(planSendTime);
			boolean sendNow = planSendTime.compareTo(LocalDateTime.now()) <= 0;
			SubscribeTaskDetailEnum.Status status = sendNow ? SubscribeTaskDetailEnum.Status.PRE_SEND
					: SubscribeTaskDetailEnum.Status.PRE_TIME_SEND;
			subscribeTaskDetail.setStatus(status.getCode());
			subscribeTaskDetailService.save(subscribeTaskDetail);

			if (sendNow) {
				asyncExe(subscribeTaskDetail.getId(), status);
			}
		}
	}

	/* 定时任务轮询 */
	public List<Integer> select4PreTimeSend(int limit) {
		List<Integer> subscribeTaskIdList = subscribeTaskDetailService.select4PreTimeSend(SubscribeTaskDetailEnum.Status.PRE_TIME_SEND,
				limit);
		return subscribeTaskIdList;
	}

	public void exePreTimeSend(Integer id) {
		asyncExe(id, SubscribeTaskDetailEnum.Status.PRE_TIME_SEND);
	}
	/* 定时任务轮询 */

	private void asyncExe(Integer subscribeTaskDetailId, SubscribeTaskDetailEnum.Status status) {
		// MQ异步处理
		SendSubscribeMQDto params = new SendSubscribeMQDto();
		params.setSubscribeTaskDetailId(subscribeTaskDetailId);

		messageSender.sendNormalMessage(StrategyConstants.SENDSUBSCRIBE_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.SEND_SUBSCRIBE.ROUTING_KEY);

		// 必须加状态条件，消费者代码可能会比下面的代码先执行
		SubscribeTaskDetail subscribeTaskDetail = subscribeTaskDetailService.getById(subscribeTaskDetailId);
		String remark = Utils.rightRemark(subscribeTaskDetail.getRemark(),
				SubscribeTaskDetailEnum.Status.SEND_MQ_SUCCESS.getDesc());
		subscribeTaskDetailService.updateStatusByStatus(SubscribeTaskDetailEnum.Status.SEND_MQ_SUCCESS, remark, subscribeTaskDetailId,
				status);
	}
}
