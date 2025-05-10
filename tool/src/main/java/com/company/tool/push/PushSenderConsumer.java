package com.company.tool.push;

import cn.hutool.core.date.DateUtil;
import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.tool.entity.PushTask;
import com.company.tool.entity.PushTaskDetail;
import com.company.tool.entity.PushTemplate;
import com.company.tool.enums.PushTaskDetailEnum;
import com.company.tool.push.core.PushSender;
import com.company.tool.push.core.SendResponse;
import com.company.tool.service.PushTaskDetailService;
import com.company.tool.service.PushTaskService;
import com.company.tool.service.PushTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 推送发送处理器（消费者逻辑）
 */
@Slf4j
@Component
public class PushSenderConsumer {

	@Autowired
	private PushTaskService pushTaskService;
	@Autowired
	private PushTaskDetailService pushTaskDetailService;
	@Autowired
	private PushTemplateService pushTemplateService;
	@Autowired
	private PushSender pushSender;

	public void consumer(Integer pushTaskDetailId) {
		PushTaskDetail pushTaskDetail = pushTaskDetailService.getById(pushTaskDetailId);
		PushTask pushTask = pushTaskService.getById(pushTaskDetail.getTaskId());
		String remark = pushTaskDetail.getRemark();

		Integer status = pushTaskDetail.getStatus();
		PushTaskDetailEnum.Status statusEnum = PushTaskDetailEnum.Status.of(status);

		if (statusEnum == PushTaskDetailEnum.Status.REQ_SUCCESS || statusEnum == PushTaskDetailEnum.Status.REQ_FAIL
				|| statusEnum == PushTaskDetailEnum.Status.SEND_CANCEL) {
			log.info("状态不匹配 {}:{}", pushTaskDetailId, status);
			remark = Utils.rightRemark(remark, "状态不匹配:" + statusEnum.getDesc());
			pushTaskDetailService.updateRemark(remark, pushTaskDetailId);
			return;
		}

		remark = Utils.rightRemark(remark, PushTaskDetailEnum.Status.CONS_MQ_SUCCESS.getDesc());
		pushTaskDetailService.updateStatusRemark(PushTaskDetailEnum.Status.CONS_MQ_SUCCESS, remark, pushTaskDetailId);

		LocalDateTime overTime = pushTask.getOverTime();
		LocalDateTime now = LocalDateTime.now();
		if (overTime.compareTo(now) < 0) {
			log.info("超时取消发送 {}:{} {}/{}", pushTaskDetailId, pushTaskDetail.getDeviceid(),
					DateUtil.formatLocalDateTime(overTime), DateUtil.formatLocalDateTime(now));
			remark = Utils.rightRemark(remark, "超时取消发送");
			pushTaskDetailService.updateStatusRemark(PushTaskDetailEnum.Status.SEND_CANCEL, remark, pushTaskDetailId);
			return;
		}

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> templateParamMap = JsonUtil.toEntity(pushTaskDetail.getTemplateParamJson(),
				LinkedHashMap.class);

		String type = pushTask.getType();
		PushTemplate pushTemplate = pushTemplateService.selectByType(type);
		if (pushTemplate == null) {
			remark = Utils.rightRemark(remark, String.format("模板push_template未配置,type:%s", type));
			pushTaskDetailService.updateStatusRemark(PushTaskDetailEnum.Status.SEND_CANCEL, remark, pushTaskDetailId);
			return;
		}

		String templateTitle = pushTemplate.getTemplateTitle();
		String templateContent = pushTemplate.getTemplateContent();
		String content = fillTemplateContent(templateContent, templateParamMap);
		String templateIntent = pushTemplate.getTemplateIntent();
		String intent = fillTemplateContent(templateIntent, templateParamMap);
		pushTaskDetailService.updateContentById(content, pushTaskDetailId);

		// 判断是否在线，在线则推内部消息

		// 调用推送发送API
		SendResponse response = pushSender.send(pushTaskDetail.getDeviceid(), templateTitle, content, intent);
		if (response.isSuccess()) {
			remark = Utils.rightRemark(remark, PushTaskDetailEnum.Status.REQ_SUCCESS.getDesc());
			pushTaskDetailService.updateSendSuccessStatus(PushTaskDetailEnum.Status.REQ_SUCCESS, remark, pushTaskDetailId);
		} else {
			remark = Utils.rightRemark(remark, response.getMessage());
			pushTaskDetailService.updateStatusRemark(PushTaskDetailEnum.Status.REQ_FAIL, remark, pushTaskDetailId);
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
