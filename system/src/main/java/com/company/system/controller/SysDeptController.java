package com.company.system.controller;

import java.util.List;

import com.company.common.request.RemoveReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.company.common.api.Result;
import com.company.common.response.PageResp;
import com.company.common.util.PropertyUtils;
import com.company.system.api.feign.SysDeptFeign;
import com.company.system.api.request.SysDeptReq;
import com.company.system.api.response.SysDeptResp;
import com.company.system.entity.SysDept;
import com.company.system.service.SysDeptService;

@RestController
@RequestMapping("/sysDept")
public class SysDeptController implements SysDeptFeign {

	@Autowired
	private SysDeptService sysDeptService;

	private QueryWrapper<SysDept> toQueryWrapper(Integer parentId, String parentIds, String name, Integer orderNum, String status) {
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		if (parentId != null) {
			queryWrapper.eq("parent_id", parentId);
		}
		if (StringUtils.isNotBlank(parentIds)) {
			queryWrapper.like("parent_ids", parentIds);
		}
		if (StringUtils.isNotBlank(name)) {
			queryWrapper.like("name", name);
		}
		if (orderNum != null) {
			queryWrapper.eq("order_num", orderNum);
		}
		if (status != null) {
			queryWrapper.eq("status", status);
		}
		return queryWrapper;
	}
	
	@Override
	public Result<PageResp<SysDeptResp>> page(Long current, Long size, Integer parentId, String parentIds, String name, Integer orderNum, String status) {
		QueryWrapper<SysDept> queryWrapper = toQueryWrapper(parentId, parentIds, name, orderNum, status);
		
		long count = sysDeptService.count(queryWrapper);
		
		queryWrapper.orderByDesc("id");
		List<SysDept> list = sysDeptService.list(PageDTO.of(current, size), queryWrapper);

		List<SysDeptResp> respList = PropertyUtils.copyArrayProperties(list, SysDeptResp.class);
		return Result.success(PageResp.of(count, respList));
	}
	
	@Override
	public Result<List<SysDeptResp>> list(Integer parentId, String parentIds, String name, Integer orderNum, String status) {
		QueryWrapper<SysDept> queryWrapper = toQueryWrapper(parentId, parentIds, name, orderNum, status);
		
		queryWrapper.orderByDesc("id");
		List<SysDept> list = sysDeptService.list(queryWrapper);
		
		List<SysDeptResp> respList = PropertyUtils.copyArrayProperties(list, SysDeptResp.class);
		return Result.success(respList);
	}

	@Override
	public Result<SysDeptResp> query(Integer id) {
		SysDept sysDept = sysDeptService.getById(id);
		SysDeptResp sysDeptResp = PropertyUtils.copyProperties(sysDept, SysDeptResp.class);
		return Result.success(sysDeptResp);
	}

	@Override
	public Result<Boolean> save(SysDeptReq sysDeptReq) {
		SysDept sysDept = PropertyUtils.copyProperties(sysDeptReq, SysDept.class);
		boolean success = sysDeptService.save(sysDept);
		return Result.success(success);
	}

	@Override
	public Result<Boolean> update(SysDeptReq sysDeptReq) {
		SysDept sysDept = PropertyUtils.copyProperties(sysDeptReq, SysDept.class);
		boolean success = sysDeptService.updateById(sysDept);
		return Result.success(success);
	}

	@Override
	public Result<Boolean> remove(RemoveReq<Integer> req) {
		if (req.getIdList() == null || req.getIdList().isEmpty()) {
			return Result.success(true);
		}
		boolean success = sysDeptService.removeBatchByIds(req.getIdList());
		return Result.success(success);
	}
}