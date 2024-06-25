package com.company.job.service;

import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DemoService {

	@XxlJob("demoJobHandler")
	public ReturnT<String> demoJobHandler() {
		String param = XxlJobHelper.getJobParam();
		XxlJobHelper.log("param:{}", param);
		log.info("param:{}", param);
		return ReturnT.SUCCESS;
	}
}
