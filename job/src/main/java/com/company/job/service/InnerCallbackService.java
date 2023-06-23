package com.company.job.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * 内部回调补偿定时器
 */
@Component
public class InnerCallbackService {

	@Autowired
	private com.company.tool.api.feign.InnerCallbackFeign toolInnerCallbackFeign;
	@Autowired
	private com.company.order.api.feign.InnerCallbackFeign orderInnerCallbackFeign;

	@XxlJob("toolInnerCallbackHandler")
	public ReturnT<String> toolInnerCallbackHandler(String param) throws Exception {
		XxlJobHelper.log("param:{}", param);
		List<Integer> idList = null;
		if (StringUtils.isNotBlank(param)) {
			idList = Arrays.asList(param.split(",")).stream().map(String::valueOf).map(Integer::valueOf)
					.collect(Collectors.toList());
		} else {
			idList = toolInnerCallbackFeign.selectId4CallbackFail().dataOrThrow();
		}

		XxlJobHelper.log("size:{}", idList.size());
		for (Integer id : idList) {
			toolInnerCallbackFeign.postRestTemplate(id);
		}

		return ReturnT.SUCCESS;
	}
	
	@XxlJob("orderInnerCallbackHandler")
	public ReturnT<String> orderInnerCallbackHandler(String param) throws Exception {
		XxlJobHelper.log("param:{}", param);
		List<Integer> idList = null;
		if (StringUtils.isNotBlank(param)) {
			idList = Arrays.asList(param.split(",")).stream().map(String::valueOf).map(Integer::valueOf)
					.collect(Collectors.toList());
		} else {
			idList = orderInnerCallbackFeign.selectId4CallbackFail().dataOrThrow();
		}
		
		XxlJobHelper.log("size:{}", idList.size());
		for (Integer id : idList) {
			orderInnerCallbackFeign.postRestTemplate(id);
		}
		
		return ReturnT.SUCCESS;
	}
}
