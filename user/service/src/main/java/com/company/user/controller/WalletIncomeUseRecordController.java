package com.company.user.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.company.user.api.feign.WalletIncomeUseRecordFeign;
import com.company.user.service.WalletIncomeUseRecordService;

@RestController
@RequestMapping("/walletIncomeUseRecord")
public class WalletIncomeUseRecordController implements WalletIncomeUseRecordFeign {

	@Autowired
	private WalletIncomeUseRecordService walletIncomeUseRecordService;

	@Override
	public List<Integer> selectId4Expire(Integer limit) {
		List<Integer> idList = walletIncomeUseRecordService.selectId4Expire(limit);
		return idList;
	}

	@Override
	public Boolean update4Expire(Integer id) {
		Boolean success = walletIncomeUseRecordService.update4Expire(id);
		return success;
	}

}
