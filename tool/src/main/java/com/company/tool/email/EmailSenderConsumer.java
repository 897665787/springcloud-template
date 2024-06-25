package com.company.tool.email;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.tool.entity.EmailTask;
import com.company.tool.entity.EmailTaskDetail;
import com.company.tool.entity.EmailTemplate;
import com.company.tool.enums.EmailTaskDetailEnum;
import com.company.tool.service.EmailTaskDetailService;
import com.company.tool.service.EmailTaskService;
import com.company.tool.service.EmailTemplateService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件发送处理器（消费者逻辑）
 */
@Slf4j
@Component
public class EmailSenderConsumer {

	@Autowired
	private EmailTaskService emailTaskService;
	@Autowired
	private EmailTaskDetailService emailTaskDetailService;
	@Autowired
	private EmailTemplateService emailTemplateService;
	@Autowired
	private MailSender mailSender;
	
	@Value("${yhkd.enable.closeSendEmail:false}")
	private Boolean closeSendEmail;// 关闭发送邮件
	
	public void consumer(Integer emailTaskDetailId) {
		EmailTaskDetail emailTaskDetail = emailTaskDetailService.selectById(emailTaskDetailId);
		EmailTask emailTask = emailTaskService.selectById(emailTaskDetail.getTaskId());
		String remark = emailTaskDetail.getRemark();

		Integer status = emailTaskDetail.getStatus();
		EmailTaskDetailEnum.Status statusEnum = EmailTaskDetailEnum.Status.of(status);

		if (statusEnum == EmailTaskDetailEnum.Status.REQ_SUCCESS || statusEnum == EmailTaskDetailEnum.Status.REQ_FAIL
				|| statusEnum == EmailTaskDetailEnum.Status.SEND_CANCEL) {
			log.info("状态不匹配 {}:{}", emailTaskDetailId, status);
			remark = Utils.rightRemark(remark, "状态不匹配:" + statusEnum.getDesc());
			emailTaskDetailService.updateRemark(remark, emailTaskDetailId);
			return;
		}

		remark = Utils.rightRemark(remark, EmailTaskDetailEnum.Status.CONS_MQ_SUCCESS.getDesc());
		emailTaskDetailService.updateStatusRemark(EmailTaskDetailEnum.Status.CONS_MQ_SUCCESS, remark,
				emailTaskDetailId);

		LocalDateTime overTime = emailTask.getOverTime();
		LocalDateTime now = LocalDateTime.now();
		if (overTime.compareTo(now) < 0) {
			log.info("超时取消发送 {}:{} {}/{}", emailTaskDetailId, emailTaskDetail.getToEmail(),
					DateUtil.formatLocalDateTime(overTime), DateUtil.formatLocalDateTime(now));
			remark = Utils.rightRemark(remark, "超时取消发送");
			emailTaskDetailService.updateStatusRemark(EmailTaskDetailEnum.Status.SEND_CANCEL, remark,
					emailTaskDetailId);
			return;
		}

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> templateParamMap = JsonUtil.toEntity(emailTaskDetail.getTemplateParamJson(),
				LinkedHashMap.class);

		String type = emailTask.getType();

		EmailTemplate emailTemplate = emailTemplateService.selectByType(type);
		if (emailTemplate == null) {
			remark = Utils.rightRemark(remark, String.format("模板email_template未配置,type:%s", type));
			emailTaskDetailService.updateStatusRemark(EmailTaskDetailEnum.Status.SEND_CANCEL, remark,
					emailTaskDetailId);
			return;
		}
		
		String title = emailTemplate.getTemplateTitle();
		String templateContent = emailTemplate.getTemplateContent();
		String content = fillTemplateContent(templateContent, templateParamMap);
		emailTaskDetailService.updateTitleContentById(title, content, emailTaskDetailId);

		// 调用邮件发送API
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setSubject(title);// 标题
		simpleMessage.setText(content);// 内容
		simpleMessage.setFrom(emailTaskDetail.getFromEmail());// 发送人邮箱
		simpleMessage.setTo(emailTaskDetail.getToEmail());// 发送目的地邮箱

		try {
			if (closeSendEmail) {
				log.warn("closeSendEmail,not exec mailSender.send");
			} else {
				mailSender.send(simpleMessage);
			}
			remark = Utils.rightRemark(remark, EmailTaskDetailEnum.Status.REQ_SUCCESS.getDesc());
			emailTaskDetailService.updateSendSuccessStatus(EmailTaskDetailEnum.Status.REQ_SUCCESS, remark,
					emailTaskDetailId);
		} catch (MailException e) {
			log.error("send error", e);
			remark = Utils.rightRemark(remark, e.getMessage());
			emailTaskDetailService.updateStatusRemark(EmailTaskDetailEnum.Status.REQ_FAIL, remark, emailTaskDetailId);
		}
	}

	/**
	 * 填充模板内容
	 * 
	 * @param templateContent
	 * @param map
	 * @return
	 */
	private String fillTemplateContent(String templateContent, LinkedHashMap<String, String> templateParamMap) {
		if (templateContent == null) {
			return null;
		}

		String content = templateContent;

		Set<Entry<String, String>> entrySet = templateParamMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String value = entry.getValue();
			content = content.replace(String.format("${%s}", key), value);
		}

		return content;
	}
}
