package com.company.{module}.controller;

import java.math.BigDecimal;
import java.util.List;

import com.company.common.request.RemoveReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.company.common.api.Result;
import com.company.common.response.PageResp;
import com.company.common.util.PropertyUtils;
import com.company.{module}.api.feign.{ModelName}Feign;
import com.company.{module}.api.request.{ModelName}Req;
import com.company.{module}.api.response.{ModelName}Resp;
import com.company.{module}.entity.{ModelName};
import com.company.{module}.service.{ModelName}Service;

@RestController
@RequestMapping("/{modelName}")
public class {ModelName}Controller implements {ModelName}Feign {

	@Autowired
	private {ModelName}Service {modelName}Service;

	private QueryWrapper<{ModelName}> toQueryWrapper({column_field_feign}) {
		QueryWrapper<{ModelName}> queryWrapper = new QueryWrapper<>();
{search_form}		return queryWrapper;
	}
	
	@Override
	public Result<PageResp<{ModelName}Resp>> page({page_column_field_feign}) {
		QueryWrapper<{ModelName}> queryWrapper = toQueryWrapper({column_field});
		
		long count = {modelName}Service.count(queryWrapper);
		
		queryWrapper.orderByDesc("id");
		List<{ModelName}> list = {modelName}Service.list(PageDTO.of(current, size), queryWrapper);

		List<{ModelName}Resp> respList = PropertyUtils.copyArrayProperties(list, {ModelName}Resp.class);
		return Result.success(PageResp.of(count, respList));
	}
	
	@Override
	public Result<List<{ModelName}Resp>> list({column_field_feign}) {
		QueryWrapper<{ModelName}> queryWrapper = toQueryWrapper({column_field});
		
		queryWrapper.orderByDesc("id");
		List<{ModelName}> list = {modelName}Service.list(queryWrapper);
		
		List<{ModelName}Resp> respList = PropertyUtils.copyArrayProperties(list, {ModelName}Resp.class);
		return Result.success(respList);
	}

	@Override
	public Result<{ModelName}Resp> query(Integer id) {
		{ModelName} {modelName} = {modelName}Service.getById(id);
		{ModelName}Resp {modelName}Resp = PropertyUtils.copyProperties({modelName}, {ModelName}Resp.class);
		return Result.success({modelName}Resp);
	}

	@Override
	public Result<Boolean> save({ModelName}Req {modelName}Req) {
		{ModelName} {modelName} = PropertyUtils.copyProperties({modelName}Req, {ModelName}.class);
		boolean success = {modelName}Service.save({modelName});
		return Result.success(success);
	}

	@Override
	public Result<Boolean> update({ModelName}Req {modelName}Req) {
		{ModelName} {modelName} = PropertyUtils.copyProperties({modelName}Req, {ModelName}.class);
		boolean success = {modelName}Service.updateById({modelName});
		return Result.success(success);
	}

	@Override
	public Result<Boolean> remove(@RequestBody RemoveReq<Integer> req) {
		if (req.getIdList() == null || req.getIdList().isEmpty()) {
			return Result.success(true);
		}
		boolean success = {modelName}Service.removeBatchByIds(req.getIdList());
		return Result.success(success);
	}
}