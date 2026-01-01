package com.company.job.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.company.tool.api.feign.SmsFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsService {

	private final SmsFeign smsFeign;

	@XxlJob("smsHandler")
	public ReturnT<String> smsHandler() {
		String param = XxlJobHelper.getJobParam();
		XxlJobHelper.log("param:{}", param);
		List<Integer> idList = null;
		if (StringUtils.isNotBlank(param)) {
			idList = Arrays.asList(param.split(",")).stream().map(String::valueOf).map(Integer::valueOf)
					.collect(Collectors.toList());

			XxlJobHelper.log("size:{}", idList.size());
			for (Integer id : idList) {
				smsFeign.exePreTimeSend(id);
			}
			return ReturnT.SUCCESS;
		}

		int limit = 1000;
		do {
			idList = smsFeign.select4PreTimeSend(limit);

			XxlJobHelper.log("size:{}", idList.size());
			for (Integer id : idList) {
				smsFeign.exePreTimeSend(id);
			}
		} while (idList.size() == limit);

		return ReturnT.SUCCESS;
	}
}
