package com.company.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.system.innercallback.service.IInnerCallbackService;
import com.company.tool.api.feign.InnerCallbackFeign;

@RestController
@RequestMapping(value = "/innerCallback")
public class InnerCallbackController implements InnerCallbackFeign {
	
	@Autowired
	private IInnerCallbackService innerCallbackService;

	@Override
	public Result<Boolean> postRestTemplate(Integer id) {
		Boolean success = innerCallbackService.postRestTemplate(id);
		return Result.success(success);
	}

	@Override
	public Result<List<Integer>> selectId4CallbackFail() {
		List<Integer> idList = innerCallbackService.selectId4CallbackFail();
		return Result.success(idList);
	}
}
