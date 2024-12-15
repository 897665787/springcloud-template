package com.company.tool.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.EmailTaskDetail;
import com.company.tool.enums.EmailTaskDetailEnum;
import com.company.tool.mapper.EmailTaskDetailMapper;

@Service
public class EmailTaskDetailService extends ServiceImpl<EmailTaskDetailMapper, EmailTaskDetail> {

	public Integer updateStatusByStatus(EmailTaskDetailEnum.Status toStatus, String remark, Integer id,
			EmailTaskDetailEnum.Status fromStatus) {
		return baseMapper.updateStatusByStatus(toStatus, remark, id, fromStatus);
	}

	public Integer updateTitleContentById(String title, String content, Integer id) {
		return baseMapper.updateTitleContentById(title, content, id);
	}

	public Integer updateSendSuccessStatus(EmailTaskDetailEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateSendSuccessStatus(toStatus, remark, id);
	}

	public Integer updateStatusRemark(EmailTaskDetailEnum.Status toStatus, String remark, Integer id) {
		return baseMapper.updateStatusRemark(toStatus, remark, id);
	}

	public Integer updateRemark(String remark, Integer id) {
		return baseMapper.updateRemark(remark, id);
	}

	public List<Integer> select4PreTimeSend(EmailTaskDetailEnum.Status status, int limit) {
		return baseMapper.select4PreTimeSend(status, limit);
	}
}
