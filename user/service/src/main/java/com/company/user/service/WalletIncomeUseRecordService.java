package com.company.user.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.util.Utils;
import com.company.framework.context.SpringContextUtil;
import com.company.user.api.enums.WalletEnum;
import com.company.user.api.enums.WalletRecordEnum;
import com.company.user.entity.Wallet;
import com.company.user.entity.WalletIncomeUseRecord;
import com.company.user.entity.WalletRecord;
import com.company.user.mapper.wallet.WalletIncomeUseRecordMapper;
import com.company.user.wallet.Expire;
import com.company.user.wallet.dto.WalletId;
import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;

@Service
public class WalletIncomeUseRecordService extends ServiceImpl<WalletIncomeUseRecordMapper, WalletIncomeUseRecord>
		implements IService<WalletIncomeUseRecord> {

	@Autowired
	private WalletRecordService walletRecordService;
	@Autowired
	private WalletService walletService;

	public void save(Integer walletRecordId, BigDecimal amount, Integer expireMinutes, String beanName) {
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		WalletIncomeUseRecord walletIncomeUseRecord = baseMapper.selectByWalletRecordId(walletRecordId);
		if (walletIncomeUseRecord != null) {
			return;
		}

		WalletRecord walletRecord = walletRecordService.getById(walletRecordId);

		WalletIncomeUseRecord record = new WalletIncomeUseRecord();
		record.setWalletId(walletRecord.getWalletId());
		record.setWalletRecordId(walletRecordId);
		record.setUnusedAmount(amount);
		record.setAmount(amount);
		record.setStatus(WalletRecordEnum.IncomeRecordUseStatus.UN_USED.getCode());
		LocalDateTime invalidTime = LocalDateTime.now().plusMinutes(expireMinutes);
		record.setInvalidTime(invalidTime);
		record.setBeanName(beanName);
		baseMapper.insert(record);
	}

	public void use(String uniqueCode, Integer userId, WalletEnum.Type type, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		// TODO 这里不好做幂等
		Wallet wallet = walletService.getOrInit(userId, type);
		List<WalletIncomeUseRecord> walletIncomeUseRecordList = baseMapper.select2Use(wallet.getId());
		if (walletIncomeUseRecordList.isEmpty()) {
			return;
		}

		BigDecimal bamount = amount;

		for (WalletIncomeUseRecord walletIncomeUseRecord : walletIncomeUseRecordList) {
			BigDecimal unusedAmount = walletIncomeUseRecord.getUnusedAmount();

			WalletRecordEnum.IncomeRecordUseStatus toStatus = null;
			BigDecimal usedAmount = null;
			if (bamount.compareTo(unusedAmount) >= 0) {
				toStatus = WalletRecordEnum.IncomeRecordUseStatus.ALL_USED;
				usedAmount = unusedAmount;
			} else {
				toStatus = WalletRecordEnum.IncomeRecordUseStatus.SUB_USED;
				usedAmount = bamount;
			}

			String remark = uniqueCode + "," + DateUtil.now() + "使用" + usedAmount;
			remark = Utils.rightRemark(walletIncomeUseRecord.getRemark(), remark);

			Integer affect = baseMapper.update2use(usedAmount, toStatus, remark, walletIncomeUseRecord.getId());
			if (affect > 0) {// 极端情况下，如果修改失败，需要跳过这条数据，寻找下一条进行修改
				bamount = bamount.subtract(unusedAmount);
			}
			if (bamount.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}
		}
	}

	public List<Integer> selectId4Expire(Integer limit) {
		return baseMapper.selectId4Expire(limit);
	}

	public Boolean update4Expire(Integer id) {
		Integer affect = baseMapper.update4Expire(WalletRecordEnum.IncomeRecordUseStatus.EXPIRED, id);
		if (affect == 0) {
			return false;
		}

		WalletIncomeUseRecord record = baseMapper.selectById(id);
		if (StringUtils.isBlank(record.getBeanName())) {
			return true;
		}
		Expire expire = SpringContextUtil.getBean(record.getBeanName(), Expire.class);
		if (expire == null) {
			return true;
		}

		Integer walletId = record.getWalletId();
		Wallet wallet = walletService.getById(walletId);

		Map<String, Object> attachMap = Maps.newHashMap();
		attachMap.put("walletIncomeUseRecordId", id);

		// 回收未用完的奖励
		BigDecimal expireAmount = record.getUnusedAmount();
		String uniqueCode = "walletExpire-" + id;

		// 回收余额
		expire.exeOutcome(uniqueCode, new WalletId(wallet.getUserId(), WalletEnum.Type.of(wallet.getType())),
				expireAmount, attachMap);// 如果账对不齐，可能会出现余额不足的情况，不处理
		return true;
	}
}
