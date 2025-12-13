package com.company.tool.email;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.company.framework.messagedriven.properties.MessagedrivenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.framework.util.Utils;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.context.SpringContextUtil;
import com.company.tool.messagedriven.Constants;
import com.company.tool.messagedriven.strategy.StrategyConstants;
import com.company.tool.messagedriven.strategy.dto.SendEmailMQDto;
import com.company.tool.api.enums.EmailEnum;
import com.company.tool.email.dto.EmailTemplateParam;
import com.company.tool.entity.EmailTask;
import com.company.tool.entity.EmailTaskDetail;
import com.company.tool.enums.EmailTaskDetailEnum;
import com.company.tool.service.EmailTaskDetailService;
import com.company.tool.service.EmailTaskService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;

/**
 * 邮件发送器（异步）
 */
@Component
public class AsyncEmailSender {

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private MessagedrivenProperties messagedrivenProperties;
	@Autowired
	private EmailTaskService emailTaskService;
	@Autowired
	private EmailTaskDetailService emailTaskDetailService;

	public void send0(List<String> emailList, EmailEnum.Type type) {
		Map<String, String> emptyMap = Maps.newHashMap();
		List<EmailTemplateParam> emailTemplateParamList = emailList.stream().map(email -> {
			EmailTemplateParam emailTemplateParam = new EmailTemplateParam();
			emailTemplateParam.setEmail(email);
			emailTemplateParam.setTemplateParamMap(emptyMap);
			return emailTemplateParam;
		}).collect(Collectors.toList());
		send(emailTemplateParamList, type);
	}

	public void send(List<EmailTemplateParam> emailTemplateParamList, EmailEnum.Type type) {
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusDays(1);
		send(emailTemplateParamList, type, planSendTime, overTime);
	}

	public void send(String email, EmailEnum.Type type) {
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusDays(1);
		send(email, Maps.newHashMap(), type, planSendTime, overTime);
	}

	public void send(String email, EmailEnum.Type type, LocalDateTime planSendTime, LocalDateTime overTime) {
		send(email, Maps.newHashMap(), type, planSendTime, overTime);
	}

	public void send(String email, Map<String, String> templateParamMap, EmailEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		EmailTemplateParam emailTemplateParam = new EmailTemplateParam();
		emailTemplateParam.setEmail(email);
		emailTemplateParam.setTemplateParamMap(templateParamMap);
		send(emailTemplateParam, type, planSendTime, overTime);
	}

	public void send(EmailTemplateParam emailTemplateParam, EmailEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {
		send(Lists.newArrayList(emailTemplateParam), type, planSendTime, overTime);
	}

	public void send(List<EmailTemplateParam> emailTemplateParamList, EmailEnum.Type type, LocalDateTime planSendTime,
			LocalDateTime overTime) {

		EmailTask emailTask = new EmailTask();
		emailTask.setType(type.getCode());
		emailTask.setPlanSendTime(planSendTime);
		emailTask.setOverTime(overTime);
		emailTask.setRemark("AsyncEmailSender-" + DateUtil.format(new Date(), "yyMMddHHmmss"));
		emailTaskService.save(emailTask);

		for (EmailTemplateParam emailTemplateParam : emailTemplateParamList) {
			EmailTaskDetail emailTaskDetail = new EmailTaskDetail();
			emailTaskDetail.setTaskId(emailTask.getId());
			String fromEmail = SpringContextUtil.getProperty("spring.mail.username");
			emailTaskDetail.setFromEmail(fromEmail);// 可能会根据收件邮箱规则去找发件邮箱
			emailTaskDetail.setToEmail(emailTemplateParam.getEmail());
			emailTaskDetail.setTemplateParamJson(JsonUtil.toJsonString(emailTemplateParam.getTemplateParamMap()));
			emailTaskDetail.setPlanSendTime(planSendTime);
			boolean sendNow = planSendTime.compareTo(LocalDateTime.now()) <= 0;
			EmailTaskDetailEnum.Status status = sendNow ? EmailTaskDetailEnum.Status.PRE_SEND
					: EmailTaskDetailEnum.Status.PRE_TIME_SEND;
			emailTaskDetail.setStatus(status.getCode());
			emailTaskDetailService.save(emailTaskDetail);

			if (sendNow) {
				asyncExe(emailTaskDetail.getId(), status);
			}
		}
	}

	/* 定时任务轮询 */
	public List<Integer> select4PreTimeSend(int limit) {
		List<Integer> emailTaskIdList = emailTaskDetailService.select4PreTimeSend(EmailTaskDetailEnum.Status.PRE_TIME_SEND,
				limit);
		return emailTaskIdList;
	}

	public void exePreTimeSend(Integer id) {
		asyncExe(id, EmailTaskDetailEnum.Status.PRE_TIME_SEND);
	}
	/* 定时任务轮询 */

	private void asyncExe(Integer emailTaskDetailId, EmailTaskDetailEnum.Status status) {
		// MQ异步处理
		SendEmailMQDto params = new SendEmailMQDto();
		params.setEmailTaskDetailId(emailTaskDetailId);

		messageSender.sendNormalMessage(StrategyConstants.SENDEMAIL_STRATEGY, params, messagedrivenProperties.getExchange().getDirect(),
				Constants.QUEUE.SEND_EMAIL.KEY);

		// 必须加状态条件，消费者代码可能会比下面的代码先执行
		EmailTaskDetail emailTaskDetail = emailTaskDetailService.getById(emailTaskDetailId);
		String remark = Utils.rightRemark(emailTaskDetail.getRemark(),
				EmailTaskDetailEnum.Status.SEND_MQ_SUCCESS.getDesc());
		emailTaskDetailService.updateStatusByStatus(EmailTaskDetailEnum.Status.SEND_MQ_SUCCESS, remark, emailTaskDetailId,
				status);
	}
}
