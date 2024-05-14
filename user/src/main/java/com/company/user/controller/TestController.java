package com.company.user.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.context.SpringContextUtil;
import com.company.user.api.enums.WalletEnum;
import com.company.user.api.enums.WalletEnum.Type;
import com.company.user.entity.City;
import com.company.user.entity.UserInfo;
import com.company.user.mapper.common.CityMapper;
import com.company.user.service.UserInfoService;
import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.MainChargeGiftAmount;
import com.company.user.wallet.dto.MainChargeGiftBalance;
import com.company.user.wallet.dto.MainChargeGiftWalletId;
import com.company.user.wallet.dto.WalletId;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/test")
public class TestController{

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private CityMapper cityMapper;

	@GetMapping(value = "/beans")
	public Result<Map<?,?>> beans() {
		ApplicationContext context = SpringContextUtil.getContext();
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		Map<String, Object> map = Maps.newHashMap();
		map.put("beanDefinitionNames", beanDefinitionNames);
		return Result.success(map);
	}
	
	@GetMapping(value = "/getById")
	public Result<UserInfo> getById(Integer id) {
		UserInfo userInfo = userInfoService.selectById(id);
		return Result.success(userInfo);
	}
	
	@GetMapping(value = "/save")
	public Result<Void> save(Integer id) {
		UserInfo entity = new UserInfo();
		entity.setNickname("1111");
		userInfoService.insert(entity);
		return Result.success();
	}
	
	@GetMapping(value = "/city")
	public Result<City> city(Integer id) {
		City selectById = cityMapper.selectById(id);
		return Result.success(selectById);
	}
	
	@Autowired
	private IWallet<WalletId, BigDecimal, BigDecimal, BigDecimal> normalWallet;

	@GetMapping(value = "/normal/income")
	public Result<?> normal_income(Integer userId) {
		WalletId walletId = new WalletId(userId, Type.INVITE_REWARD);

		BigDecimal amount = new BigDecimal("12.55");

		Map<String, Object> attachMap = Maps.newHashMap();
		attachMap.put("businessType", "INVITE_REWARD");
		attachMap.put("businessId", 1);

		String uniqueCode = "INVITE_REWARD_" + 2;
		normalWallet.income(uniqueCode, walletId, amount, attachMap);

		return Result.success();
	}

	@GetMapping(value = "/normal/outcome")
	public Result<?> normal_outcome(Integer userId) {
		WalletId walletId = new WalletId(userId, Type.INVITE_REWARD);
		
		BigDecimal amount = new BigDecimal("1.55");
		
		Map<String, Object> attachMap = Maps.newHashMap();
		attachMap.put("businessType", "WITHDRAW");
		attachMap.put("businessId", 2);
		
		String uniqueCode = "WITHDRAW_" + 2;
		normalWallet.outcome(uniqueCode, walletId, amount, attachMap);
		return Result.success();
	}
	
	@Autowired
	private IWallet<MainChargeGiftWalletId, MainChargeGiftAmount, MainChargeGiftAmount, MainChargeGiftBalance> mainChargeGiftWallet;
	
	@GetMapping(value = "/mainChargeGift/income")
	public Result<?> mainChargeGift_income(Integer userId) {
		MainChargeGiftWalletId walletId = new MainChargeGiftWalletId(userId, WalletEnum.Type.TO_MAIN,
				WalletEnum.Type.TO_CHARGE, WalletEnum.Type.TO_GIFT);

		MainChargeGiftAmount amount = new MainChargeGiftAmount(new BigDecimal("10").add(new BigDecimal("2")),
				new BigDecimal("10"), new BigDecimal("2"));

		Map<String, Object> attachMap = Maps.newHashMap();
		attachMap.put("businessType", "INVITE_REWARD");
		attachMap.put("businessId", 1);

		String uniqueCode = "WITHDRAW_" + 2;
		mainChargeGiftWallet.income(uniqueCode, walletId, amount, attachMap);

		return Result.success();
	}

	@GetMapping(value = "/mainChargeGift/outcome")
	public Result<?> mainChargeGift_outcome(Integer userId) {
		MainChargeGiftWalletId walletId = new MainChargeGiftWalletId(userId, WalletEnum.Type.TO_MAIN,
				WalletEnum.Type.TO_CHARGE, WalletEnum.Type.TO_GIFT);

		MainChargeGiftAmount amount = new MainChargeGiftAmount(new BigDecimal("2").add(new BigDecimal("1")),
				new BigDecimal("2"), new BigDecimal("1"));
		
		Map<String, Object> attachMap = Maps.newHashMap();
		attachMap.put("businessType", "WITHDRAW");
		attachMap.put("businessId", 2);
		
		String uniqueCode = "WITHDRAW_" + 2;
		mainChargeGiftWallet.outcome(uniqueCode, walletId, amount, attachMap);
		return Result.success();
	}
}
