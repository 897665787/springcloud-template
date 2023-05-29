package com.company.tool.sms;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.entity.SmsRecord;
import com.company.tool.entity.SmsTemplate;
import com.company.tool.service.SmsRecordService;
import com.company.tool.service.SmsTemplateService;
import com.jqdi.smssender.core.SendPostProcessor;
import com.jqdi.smssender.core.SendResponse;

@Component
public class MysqlSendPostProcessor implements SendPostProcessor {

	@Autowired
	private SmsTemplateService smsTemplateService;
	@Autowired
	private SmsRecordService smsRecordService;

	@Override
	public void afterSend(String channel, String mobile, String signName, String templateCode,
			LinkedHashMap<String, String> templateParamMap, SendResponse sendResponse) {

		SmsRecord smsRecord = new SmsRecord();
		smsRecord.setChannel(channel);
		smsRecord.setMobile(mobile);
		smsRecord.setSignName(signName);
		smsRecord.setTemplateCode(templateCode);
		smsRecord.setTemplateParamJson(JsonUtil.toJsonString(templateParamMap));

		SmsTemplate smsTemplate = smsTemplateService.selectByChannelTemplateCode(channel, templateCode);
		if (smsTemplate != null) {
			String templateContent = smsTemplate.getTemplateContent();
			String content = fillTemplateContent(templateContent, templateParamMap);
			smsRecord.setContent(content);
		} else {
			String content = String.format("模板sms_template未配置,channel:%s,templateCode:%s", channel, templateCode);
			smsRecord.setContent(content);
		}

		if (sendResponse.isSuccess()) {
			smsRecord.setResult("success");
		} else {
			smsRecord.setResult("fail");
			smsRecord.setMessage(sendResponse.getMessage());
		}
		smsRecord.setRequestId(sendResponse.getRequestId());
		smsRecordService.insert(smsRecord);
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
