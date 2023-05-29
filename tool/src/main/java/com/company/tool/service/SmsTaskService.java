package com.company.tool.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.SmsTask;
import com.company.tool.enums.SmsTaskEnum;
import com.company.tool.mapper.SmsTaskMapper;

@Service
public class SmsTaskService extends ServiceImpl<SmsTaskMapper, SmsTask> {

	public Integer updateStatusByStatus(SmsTaskEnum.Status toStatus, Integer id, SmsTaskEnum.Status fromStatus) {
		return baseMapper.updateStatusByStatus(toStatus, id, fromStatus);
	}

	public Integer updateSendSuccessStatus(SmsTaskEnum.Status toStatus, Integer id) {
		return baseMapper.updateSendSuccessStatus(toStatus, id);
	}

	public Integer updateStatusRemark(SmsTaskEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateStatusRemark(toStatus, remark, id);
	}

	public Integer updateRemark(String remark, Integer id) {
		return baseMapper.updateRemark(remark, id);
	}

	public List<Integer> select4PreTimeSend(SmsTaskEnum.Status status) {
		return baseMapper.select4PreTimeSend(status);
	}
}
