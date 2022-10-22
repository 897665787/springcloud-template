package com.company.admin.service.user.wallet;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.company.admin.annotation.XSTransactional;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.entity.user.wallet.Withdrawal;
import com.company.common.exception.BusinessException;
import com.company.admin.mapper.user.wallet.WithdrawalDao;
import com.company.admin.service.security.SecStaffService;

/**
 * 提现Service
 * Created by xuxiaowei on 2018/11/23.
 */
@Service
public class WithdrawalService {

	@Autowired
	private WithdrawalDao withdrawalDao;

	@Autowired
	private WalletService walletService;

    @Autowired
    private SecStaffService secStaffService;
    
	public void remove(Withdrawal withdrawal) {
		Withdrawal existent = get(withdrawal);
		withdrawalDao.remove(existent);
	}

	@XSTransactional
	public void update(Withdrawal withdrawal) {
		Withdrawal existent = get(withdrawal);
		SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
		withdrawal.setAuditorId(secStaff.getId());
		withdrawal.setAuditorName(secStaff.getNickname());
		if (withdrawal.getStatus() == 2) {
			withdrawal.setPassTime(new Date());
			withdrawal.setRejectReason("");
			walletService.update(existent.getUserId(), new BigDecimal(-1).multiply(existent.getFee()),
					existent.getWalletType(), 3, existent.getId());
		}
		withdrawalDao.update(withdrawal);
	}

	public Withdrawal get(Withdrawal withdrawal) {
		Withdrawal existent = withdrawalDao.get(withdrawal);
		if (existent == null) {
			throw new BusinessException("提现申请不存在");
		}
		return existent;
	}

	public XSPageModel<Withdrawal> listAndCount(Withdrawal withdrawal) {
		return XSPageModel.build(withdrawalDao.list(withdrawal), withdrawalDao.count(withdrawal));
	}
}
