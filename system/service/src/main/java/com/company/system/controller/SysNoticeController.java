package com.company.system.controller;

/**
 * 通知公告
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;

import com.company.common.response.PageResp;
import com.company.framework.util.PropertyUtils;
import com.company.system.api.interfaces.SysNoticeApi;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysNoticeReq;
import com.company.system.api.response.SysNoticeResp;
import com.company.system.entity.SysNotice;
import com.company.system.service.SysNoticeService;

/**
 * 通知公告
 */
@RestController
@RequestMapping("/sysNotice")
public class SysNoticeController implements SysNoticeApi {

	@Autowired
	private SysNoticeService sysNoticeService;

	/**
	 * 构建查询条件
	 */
	private QueryWrapper<SysNotice> toQueryWrapper(String noticeTitle, String noticeType, String createBy, String status) {
		QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(noticeTitle)) {
			queryWrapper.like("notice_title", noticeTitle);
		}
		if (StringUtils.isNotBlank(noticeType)) {
			queryWrapper.eq("notice_type", noticeType);
		}
		if (StringUtils.isNotBlank(createBy)) {
			queryWrapper.like("create_by", createBy);
		}
		if (StringUtils.isNotBlank(status)) {
			queryWrapper.eq("status", status);
		}
		return queryWrapper;
	}

	@Override
	public PageResp<SysNoticeResp> page(Long current, Long size, String noticeTitle, String noticeType, String createBy, String status) {
		QueryWrapper<SysNotice> queryWrapper = toQueryWrapper(noticeTitle, noticeType, createBy, status);
		long count = sysNoticeService.count(queryWrapper);

		queryWrapper.orderByDesc("id");
		List<SysNotice> list = sysNoticeService.list(PageDTO.of(current, size), queryWrapper);

		List<SysNoticeResp> respList = PropertyUtils.copyArrayProperties(list, SysNoticeResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public SysNoticeResp query(Integer id) {
		SysNotice sysNotice = sysNoticeService.getById(id);
		return PropertyUtils.copyProperties(sysNotice, SysNoticeResp.class);
	}

	@Override
	public Boolean save(SysNoticeReq sysNoticeReq) {
		SysNotice sysNotice = PropertyUtils.copyProperties(sysNoticeReq, SysNotice.class);
		return sysNoticeService.save(sysNotice);
	}

	@Override
	public Boolean update(SysNoticeReq sysNoticeReq) {
		SysNotice sysNotice = PropertyUtils.copyProperties(sysNoticeReq, SysNotice.class);
		return sysNoticeService.updateById(sysNotice);
	}

	@Override
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		if (req.getIdList() == null || req.getIdList().isEmpty()) {
			return true;
		}
		return sysNoticeService.removeBatchByIds(req.getIdList());
	}
}
