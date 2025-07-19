package com.company.tool.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.SubscribeTaskDetail;
import com.company.tool.enums.SubscribeTaskDetailEnum;
import com.company.tool.mapper.SubscribeTaskDetailMapper;

@Service
public class SubscribeTaskDetailService extends ServiceImpl<SubscribeTaskDetailMapper, SubscribeTaskDetail> {

	public Integer updateStatusByStatus(SubscribeTaskDetailEnum.Status toStatus, String remark, Integer id,
			SubscribeTaskDetailEnum.Status fromStatus) {
		return baseMapper.updateStatusByStatus(toStatus, remark, id, fromStatus);
	}
	
	public Integer updateTemplateCodeById(String templateCode, Integer id) {
		return baseMapper.updateTemplateCodeById(templateCode, id);
	}

	public Integer updateContentById(String content, Integer id) {
		return baseMapper.updateContentById(content, id);
	}

	public Integer updateSendSuccessStatus(SubscribeTaskDetailEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateSendSuccessStatus(toStatus, remark, id);
	}

	public Integer updateStatusRemark(SubscribeTaskDetailEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateStatusRemark(toStatus, remark, id);
	}

	public Integer updateRemark(String remark, Integer id) {
		return baseMapper.updateRemark(remark, id);
	}

	public List<Integer> select4PreTimeSend(SubscribeTaskDetailEnum.Status status, int limit) {
		return baseMapper.select4PreTimeSend(status, limit);
	}
}
