package com.company.tool.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.PushTaskDetail;
import com.company.tool.enums.PushTaskDetailEnum;
import com.company.tool.mapper.PushTaskDetailMapper;

@Service
public class PushTaskDetailService extends ServiceImpl<PushTaskDetailMapper, PushTaskDetail> {

	public Integer updateStatusByStatus(PushTaskDetailEnum.Status toStatus, String remark, Integer id,
			PushTaskDetailEnum.Status fromStatus) {
		return baseMapper.updateStatusByStatus(toStatus, remark, id, fromStatus);
	}

	public Integer updateContentById(String content, Integer id) {
		return baseMapper.updateContentById(content, id);
	}

	public Integer updateSendSuccessStatus(PushTaskDetailEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateSendSuccessStatus(toStatus, remark, id);
	}

	public Integer updateStatusRemark(PushTaskDetailEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateStatusRemark(toStatus, remark, id);
	}

	public Integer updateRemark(String remark, Integer id) {
		return baseMapper.updateRemark(remark, id);
	}

	public List<Integer> select4PreTimeSend(PushTaskDetailEnum.Status status, int limit) {
		return baseMapper.select4PreTimeSend(status, limit);
	}
}
