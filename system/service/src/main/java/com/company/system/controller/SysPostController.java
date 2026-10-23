package com.company.system.controller;

/**
 * 岗位信息
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
import com.company.system.api.interfaces.SysPostApi;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.request.SysPostReq;
import com.company.system.api.response.SysPostResp;
import com.company.system.entity.SysPost;
import com.company.system.service.SysPostService;

/**
 * 岗位信息
 */
@RestController
@RequestMapping("/sysPost")
public class SysPostController implements SysPostApi {

	@Autowired
	private SysPostService sysPostService;

	/**
	 * 构建查询条件
	 */
	private QueryWrapper<SysPost> toQueryWrapper(String postCode, String postName, String status) {
		QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(postCode)) {
			queryWrapper.like("post_code", postCode);
		}
		if (StringUtils.isNotBlank(postName)) {
			queryWrapper.like("post_name", postName);
		}
		if (StringUtils.isNotBlank(status)) {
			queryWrapper.eq("status", status);
		}
		return queryWrapper;
	}

	@Override
	public PageResp<SysPostResp> page(Long current, Long size, String postCode, String postName, String status) {
		QueryWrapper<SysPost> queryWrapper = toQueryWrapper(postCode, postName, status);
		long count = sysPostService.count(queryWrapper);

		queryWrapper.orderByAsc("post_sort");
		queryWrapper.orderByDesc("id");
		List<SysPost> list = sysPostService.list(PageDTO.of(current, size), queryWrapper);

		List<SysPostResp> respList = PropertyUtils.copyArrayProperties(list, SysPostResp.class);
		return PageResp.of(count, respList);
	}

	@Override
	public List<SysPostResp> list(String postCode, String postName, String status) {
		QueryWrapper<SysPost> queryWrapper = toQueryWrapper(postCode, postName, status);
		queryWrapper.orderByAsc("post_sort");
		queryWrapper.orderByDesc("id");
		List<SysPost> list = sysPostService.list(queryWrapper);
		return PropertyUtils.copyArrayProperties(list, SysPostResp.class);
	}

	@Override
	public SysPostResp query(Integer id) {
		SysPost sysPost = sysPostService.getById(id);
		return PropertyUtils.copyProperties(sysPost, SysPostResp.class);
	}

	@Override
	public Boolean save(SysPostReq sysPostReq) {
		SysPost sysPost = PropertyUtils.copyProperties(sysPostReq, SysPost.class);
		return sysPostService.save(sysPost);
	}

	@Override
	public Boolean update(SysPostReq sysPostReq) {
		SysPost sysPost = PropertyUtils.copyProperties(sysPostReq, SysPost.class);
		return sysPostService.updateById(sysPost);
	}

	@Override
	public Boolean remove(@RequestBody RemoveReq<Integer> req) {
		if (req.getIdList() == null || req.getIdList().isEmpty()) {
			return true;
		}
		return sysPostService.removeBatchByIds(req.getIdList());
	}
}
