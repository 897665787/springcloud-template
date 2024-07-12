package com.company.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.tool.api.feign.EmailFeign;
import com.company.tool.email.AsyncEmailSender;

@RestController
@RequestMapping(value = "/email")
public class EmailController implements EmailFeign {

	@Autowired
	private AsyncEmailSender asyncEmailSender;

	@Override
	public Result<List<Integer>> select4PreTimeSend(Integer limit) {
		List<Integer> idList = asyncEmailSender.select4PreTimeSend(limit);
		return Result.success(idList);
	}
	
	@Override
	public Result<Void> exePreTimeSend(Integer id) {
		asyncEmailSender.exePreTimeSend(id);
		return Result.success();
	}
}
