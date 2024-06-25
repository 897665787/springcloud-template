package com.company.job.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.user.api.feign.WalletIncomeUseRecordFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

@Component
public class WalletIncomeUseRecordService {

	@Autowired
	private WalletIncomeUseRecordFeign walletIncomeUseRecordFeign;

	@XxlJob("walletIncomeExpireHandler")
	public ReturnT<String> walletIncomeExpireHandler() {
		String param = XxlJobHelper.getJobParam();
		XxlJobHelper.log("param:{}", param);
		List<Integer> idList = null;
		if (StringUtils.isNotBlank(param)) {
			idList = Arrays.asList(param.split(",")).stream().map(String::valueOf).map(Integer::valueOf)
					.collect(Collectors.toList());

			XxlJobHelper.log("size:{}", idList.size());
			for (Integer id : idList) {
				walletIncomeUseRecordFeign.update4Expire(id);
			}
			return ReturnT.SUCCESS;
		}

		int limit = 1000;
		do {
			idList = walletIncomeUseRecordFeign.selectId4Expire(limit).dataOrThrow();

			XxlJobHelper.log("size:{}", idList.size());
			for (Integer id : idList) {
				walletIncomeUseRecordFeign.update4Expire(id);
			}
		} while (idList.size() == limit);

		return ReturnT.SUCCESS;
	}
}
