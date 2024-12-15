package com.company.tool.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.WebhookTask;
import com.company.tool.enums.WebhookTaskEnum;
import com.company.tool.mapper.WebhookTaskMapper;

@Service
public class WebhookTaskService extends ServiceImpl<WebhookTaskMapper, WebhookTask> {

	public Integer updateStatusByStatus(WebhookTaskEnum.Status toStatus, String remark, Integer id,
			WebhookTaskEnum.Status fromStatus) {
		return baseMapper.updateStatusByStatus(toStatus, remark, id, fromStatus);
	}

	public Integer updateKeyContentMentionById(String key, String content, String mentionedList,
			String mentionedMobileList, Integer id) {
		return baseMapper.updateKeyContentMentionById(key, content, mentionedList, mentionedMobileList, id);
	}

	public Integer updateSendSuccessStatus(WebhookTaskEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateSendSuccessStatus(toStatus, remark, id);
	}

	public Integer updateStatusRemark(WebhookTaskEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateStatusRemark(toStatus, remark, id);
	}

	public Integer updateRemark(String remark, Integer id) {
		return baseMapper.updateRemark(remark, id);
	}

	public List<Integer> select4PreTimeSend(WebhookTaskEnum.Status status, int limit) {
		return baseMapper.select4PreTimeSend(status, limit);
	}
}
