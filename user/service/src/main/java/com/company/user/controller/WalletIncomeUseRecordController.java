package com.company.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.user.api.feign.WalletIncomeUseRecordFeign;
import com.company.user.service.WalletIncomeUseRecordService;

@RestController
@RequestMapping("/walletIncomeUseRecord")
public class WalletIncomeUseRecordController implements WalletIncomeUseRecordFeign {

	@Autowired
	private WalletIncomeUseRecordService walletIncomeUseRecordService;

	@Override
	public Result<List<Integer>> selectId4Expire(Integer limit) {
		List<Integer> idList = walletIncomeUseRecordService.selectId4Expire(limit);
		return Result.success(idList);
	}

	@Override
	public Result<Boolean> update4Expire(Integer id) {
		Boolean success = walletIncomeUseRecordService.update4Expire(id);
		return Result.success(success);
	}

}
