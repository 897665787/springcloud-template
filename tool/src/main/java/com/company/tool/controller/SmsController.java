package com.company.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.tool.api.feign.SmsFeign;
import com.company.tool.sms.AsyncSmsSender;

@RestController
@RequestMapping(value = "/sms")
public class SmsController implements SmsFeign {

	@Autowired
	private AsyncSmsSender asyncSmsSender;

	@Override
	public Result<List<Integer>> select4PreTimeSend(Integer limit) {
		List<Integer> idList = asyncSmsSender.select4PreTimeSend(limit);
		return Result.success(idList);
	}
	
	@Override
	public Result<Void> exePreTimeSend(Integer id) {
		asyncSmsSender.exePreTimeSend(id);
		return Result.success();
	}
}
