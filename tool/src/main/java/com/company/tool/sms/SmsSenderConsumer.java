package com.company.tool.sms;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.tool.entity.SmsTask;
import com.company.tool.entity.SmsTypeTemplateConfig;
import com.company.tool.enums.SmsTaskEnum;
import com.company.tool.service.SmsTaskService;
import com.company.tool.service.SmsTypeTemplateConfigService;
import com.jqdi.smssender.core.SendResponse;
import com.jqdi.smssender.core.SmsSender;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信发送处理器（消费者逻辑）
 */
@Slf4j
@Component
public class SmsSenderConsumer {

	@Autowired
	private SmsTaskService smsTaskService;
	@Autowired
	private SmsTypeTemplateConfigService smsTypeTemplateConfigService;
	@Autowired
	private SmsSender smsSender;

	public void consumer(Integer smsTaskId) {
		SmsTask smsTask = smsTaskService.selectById(smsTaskId);
		String remark = smsTask.getRemark();

		Integer status = smsTask.getStatus();
		SmsTaskEnum.Status statusEnum = SmsTaskEnum.Status.of(status);

		if (statusEnum == SmsTaskEnum.Status.REQ_SUCCESS || statusEnum == SmsTaskEnum.Status.REQ_FAIL
				|| statusEnum == SmsTaskEnum.Status.SEND_CANCEL) {
			log.info("状态不匹配 {}:{}", smsTaskId, status);
			remark = Utils.rightRemark(remark, "状态不匹配:" + statusEnum.getDesc());
			smsTaskService.updateRemark(remark, smsTaskId);
			return;
		}

		remark = Utils.rightRemark(remark, "MQ消费");
		smsTaskService.updateStatusRemark(SmsTaskEnum.Status.CONS_MQ_SUCCESS, remark, smsTaskId);

		LocalDateTime overTime = smsTask.getOverTime();
		LocalDateTime now = LocalDateTime.now();
		if (overTime.compareTo(now) < 0) {
			log.info("超时取消发送 {}:{} {}/{}", smsTaskId, smsTask.getMobile(), DateUtil.formatLocalDateTime(overTime),
					DateUtil.formatLocalDateTime(now));
			remark = Utils.rightRemark(remark, "超时取消发送");
			smsTaskService.updateStatusRemark(SmsTaskEnum.Status.SEND_CANCEL, remark, smsTaskId);
			return;
		}

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> templateParamMap = JsonUtil.toEntity(smsTask.getTemplateParamJson(),
				LinkedHashMap.class);

		String type = smsTask.getType();
		String channel = smsSender.channel();
		SmsTypeTemplateConfig smsTypeTemplateConfig = smsTypeTemplateConfigService
				.selectByTypeChannel(type, channel);
		if (smsTypeTemplateConfig == null) {
			log.info("业务-模板未配置:{}-{}", type, channel);
			remark = Utils.rightRemark(remark, "业务-模板未配置:" + type + "-" + channel);
			smsTaskService.updateRemark(remark, smsTaskId);
			return;
		}
		String templateCode = smsTypeTemplateConfig.getTemplateCode();

		// 调用短信发送API
		SendResponse response = smsSender.send(smsTask.getMobile(), templateCode, templateParamMap);
		if (response.isSuccess()) {
			smsTaskService.updateSendSuccessStatus(SmsTaskEnum.Status.REQ_SUCCESS, smsTaskId);
		} else {
			remark = Utils.rightRemark(remark, response.getMessage());
			smsTaskService.updateStatusRemark(SmsTaskEnum.Status.REQ_FAIL, remark, smsTaskId);
		}
	}
}
