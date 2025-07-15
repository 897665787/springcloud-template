package com.company.user.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.util.Utils;
import com.company.user.api.enums.WalletEnum;
import com.company.user.entity.WalletUseSeq;
import com.company.user.mapper.wallet.WalletUseSeqMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WalletUseSeqService extends ServiceImpl<WalletUseSeqMapper, WalletUseSeq>
		implements IService<WalletUseSeq> {

	public void save(String uniqueCode, Integer userId, WalletEnum.Type type, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		WalletUseSeq walletUseSeqDB = baseMapper.selectByUniqueCode(uniqueCode);
		if (walletUseSeqDB != null) {
			log.warn("uniqueCode:{},已保存", uniqueCode);
			return;
		}

		WalletUseSeq walletUseSeq = new WalletUseSeq();
		walletUseSeq.setUniqueCode(uniqueCode);
		walletUseSeq.setUserId(userId);
		walletUseSeq.setType(type.getCode());
		walletUseSeq.setAmount(amount);
		walletUseSeq.setLeftAmount(amount);
		baseMapper.insert(walletUseSeq);
	}

	/**
	 * 计算不同类型的钱包应该使用多少钱（建议按此方案，不一定要完全按照方案执行）
	 *
	 * @param userId
	 * @param typeList
	 * @param totalReduceAmount
	 *            总扣减金额
	 * @return
	 */
	public Map<WalletEnum.Type, BigDecimal> calcAndUse(Integer userId, List<WalletEnum.Type> typeList,
			BigDecimal totalReduceAmount) {
		Map<WalletEnum.Type, BigDecimal> typeAmountMap = typeList.stream()
				.collect(Collectors.toMap(a -> a, b -> BigDecimal.ZERO));
		if (totalReduceAmount.compareTo(BigDecimal.ZERO) == 0) {
			return typeAmountMap;
		}
		List<WalletUseSeq> walletUseSeqList = baseMapper.selectLeftByUserIdTypeList(userId, typeList);
		for (WalletUseSeq walletUseSeq : walletUseSeqList) {
			Integer id = walletUseSeq.getId();
			BigDecimal leftAmount = walletUseSeq.getLeftAmount();
			BigDecimal reduceAmount = totalReduceAmount;
			if (reduceAmount.compareTo(leftAmount) >= 0) {
				reduceAmount = leftAmount;
			}
			String remark = Utils.rightRemark(walletUseSeq.getRemark(), "扣减" + reduceAmount.toPlainString());
			Integer affect = baseMapper.decreaseLeftAmount(id, reduceAmount, remark);
			if (affect == 0) {
				log.info("扣减失败，跳过改行数据{}:{}", id, reduceAmount);
				continue;
			}

			WalletEnum.Type type = WalletEnum.Type.of(walletUseSeq.getType());
			typeAmountMap.put(type, typeAmountMap.get(type).add(reduceAmount));

			totalReduceAmount = totalReduceAmount.subtract(reduceAmount);
			if (totalReduceAmount.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
		}
		return typeAmountMap;
	}

	/**
	 * 计算归还金额（建议按此方案，不一定要完全按照方案执行）
	 *
	 * @param orderCode
	 * @param returnTotalAmount
	 * @return
	 */
	public Map<WalletEnum.Type, BigDecimal> calcAndReturn(Integer userId, List<WalletEnum.Type> typeList,
			BigDecimal returnTotalAmount) {
		Map<WalletEnum.Type, BigDecimal> typeAmountMap = typeList.stream()
				.collect(Collectors.toMap(a -> a, b -> BigDecimal.ZERO));
		if (returnTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
			return typeAmountMap;
		}
		List<WalletUseSeq> walletUseSeqList = baseMapper.selectUseByUserIdTypeList(userId, typeList);
		for (WalletUseSeq walletUseSeq : walletUseSeqList) {
			Integer id = walletUseSeq.getId();
			BigDecimal amount = walletUseSeq.getAmount();
			BigDecimal leftAmount = walletUseSeq.getLeftAmount();
			BigDecimal canReturnAmount = amount.subtract(leftAmount);

			BigDecimal returnAmount = returnTotalAmount;
			if (returnAmount.compareTo(canReturnAmount) >= 0) {
				returnAmount = canReturnAmount;
			}

			String remark = Utils.rightRemark(walletUseSeq.getRemark(), "归还" + returnAmount.toPlainString());
			Integer affect = baseMapper.returnLeftAmount(id, returnAmount, remark);
			if (affect == 0) {
				log.info("归还失败，跳过改行数据{}:{}", id, returnAmount);
				continue;
			}

			WalletEnum.Type type = WalletEnum.Type.of(walletUseSeq.getType());
			typeAmountMap.put(type, typeAmountMap.get(type).add(returnAmount));

			returnTotalAmount = returnTotalAmount.subtract(returnAmount);
			if (returnTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
		}
		return typeAmountMap;
	}
}
