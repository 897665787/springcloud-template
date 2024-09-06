package com.company.user.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.common.util.JsonUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.user.api.enums.WalletEnum;
import com.company.user.api.enums.WalletEnum.Type;
import com.company.user.entity.Wallet;
import com.company.user.entity.WalletPreincome;
import com.company.user.mapper.wallet.WalletPreincomeMapper;
import com.company.user.wallet.Preincome;
import com.company.user.wallet.dto.WalletId;

@Service
public class WalletPreincomeService extends ServiceImpl<WalletPreincomeMapper, WalletPreincome>
		implements IService<WalletPreincome> {

	@Autowired
	private WalletService walletService;

	public void save(String uniqueCode, Integer userId, WalletEnum.Type type, BigDecimal amount, String beanName,
			String attach) {
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		WalletPreincome walletPreincome = baseMapper.selectByUniqueCode(uniqueCode);
		if (walletPreincome != null) {
			return;
		}

		Wallet wallet = walletService.getOrInit(userId, type);
		WalletPreincome record = new WalletPreincome();
		record.setUniqueCode(uniqueCode);
		record.setWalletId(wallet.getId());
		record.setAmount(amount);
		record.setStatus(1);
		record.setBeanName(beanName);
		record.setAttach(attach);
		baseMapper.insert(record);
	}

	public BigDecimal sumAmount(Integer userId, Type type) {
		Wallet wallet = walletService.getOrInit(userId, type);
		return baseMapper.sumAmount(wallet.getId());
	}

	public List<Integer> selectId4Income(Integer limit) {
		return baseMapper.selectId4Income(limit);
	}

	public Boolean update4Income(Integer id) {
		Integer affect = baseMapper.update4Income(id);
		if (affect == 0) {
			return false;
		}

		WalletPreincome record = baseMapper.selectById(id);
		if (StringUtils.isBlank(record.getBeanName())) {
			return true;
		}
		Preincome preincome = SpringContextUtil.getBean(record.getBeanName(), Preincome.class);
		if (preincome == null) {
			return true;
		}

		String uniqueCode = record.getUniqueCode();

		Integer walletId = record.getWalletId();
		Wallet wallet = walletService.getById(walletId);

		BigDecimal amount = record.getAmount();

		@SuppressWarnings("unchecked")
		Map<String, Object> attachMap = JsonUtil.toEntity(record.getAttach(), Map.class);
		attachMap.put("walletPreincomeId", id);

		preincome.exeIncome(uniqueCode, new WalletId(wallet.getUserId(), WalletEnum.Type.of(wallet.getType())), amount,
				attachMap);
		return true;
	}

}
