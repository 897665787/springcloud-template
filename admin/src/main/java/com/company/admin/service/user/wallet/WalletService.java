package com.company.admin.service.user.wallet;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.company.admin.annotation.XSTransactional;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.entity.user.User;
import com.company.admin.entity.user.wallet.WalletHistory;
import com.company.common.exception.BusinessException;
import com.company.admin.mapper.user.UserDao;
import com.company.admin.mapper.user.wallet.WalletHistoryDao;
import com.company.admin.service.security.SecStaffService;
import com.company.admin.util.DescriptionUtils;
import com.company.admin.util.XSUuidUtil;

/**
 * @author xxw
 * @date 2018/11/12
 */
@Service
public class WalletService {

    @Autowired
    private WalletHistoryDao walletHistoryDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SecStaffService secStaffService;
    
    @XSTransactional
    public void update(String userId, BigDecimal fee, Integer platform, Integer eventType, String eventId) {
        User user = userDao.getAndLock(userId);
        Integer type = fee.compareTo(new BigDecimal(0)) >= 0 ? 1 : 2;
        BigDecimal changeBefore = platform == 1 ? user.getIosWallet() : user.getAndroidWallet();
        if (type == 2 && changeBefore.compareTo(fee.abs()) < 0) {
            throw new BusinessException("余额不足");
        }
        SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        WalletHistory history = new WalletHistory()
                .setId(XSUuidUtil.generate())
                .setUserId(userId)
                .setPlatform(platform)
                .setFee(fee.abs())
                .setType(type)
                .setEventType(eventType)
                .setEventId(eventId)
                .setEventDesc(DescriptionUtils.description(WalletHistory.class, "eventType", eventType))
                .setCompleteTime(new Date())
                .setChangeBefore(changeBefore)
                .setChangeAfter(changeBefore.add(fee))
                .setCreatorId(secStaff.getId())
                .setCreator(secStaff.getNickname());
        history.setCreateTime(new Date());
        walletHistoryDao.save(history);

        if (platform == 1) {
            userDao.updateIOSWallet(userId, fee);
        }
        else if (platform == 2) {
            userDao.updateAndroidWallet(userId, fee);
        }
    }

    public XSPageModel<WalletHistory> listHistory(User user) {
        return XSPageModel.build(walletHistoryDao.list(user), walletHistoryDao.count(user));
    }
}
