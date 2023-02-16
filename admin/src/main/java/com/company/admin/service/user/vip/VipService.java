package com.company.admin.service.user.vip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.entity.user.User;
import com.company.admin.entity.user.vip.VipHistory;
import com.company.admin.mapper.user.UserDao;
import com.company.admin.mapper.user.vip.VipHistoryDao;
import com.company.admin.service.security.SecStaffService;
import com.company.admin.util.DateUtils;
import com.company.admin.util.XSUuidUtil;
import com.company.common.exception.BusinessException;

/**
 * @author xxw
 * @date 2018/11/15
 */
@Service
public class VipService {

    @Autowired
    private VipHistoryDao vipHistoryDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SecStaffService secStaffService;

    @Transactional
    public void update(String userId, Integer duration) {
        User user = userDao.getAndLock(userId);
        Date changeAfter;
        boolean isVip;
        if (duration <= 0) {
            if (user.getVipExpire() == null) {
                throw new BusinessException("用户不是会员");
            }
            else {
                long restDays = LocalDate.now().until(DateUtils.dateToLocalDate(user.getVipExpire()), ChronoUnit.DAYS) + 1;
                if (Math.abs(duration) >= restDays) {
                    duration = new Long(restDays).intValue() * -1;
                    changeAfter = null;
                    isVip = false;
                }
                else {
                    changeAfter = DateUtils.LocalDateToDate(DateUtils.dateToLocalDate(user.getVipExpire())
                            .plusDays(duration.longValue()));
                    isVip = true;
                }
            }
        }
        else {
            if (user.getVipExpire() == null) {
                changeAfter = DateUtils.LocalDateToDate(LocalDate.now().plusDays(duration.longValue() - 1L));
            }
            else {
                changeAfter = DateUtils.LocalDateToDate(DateUtils.dateToLocalDate(user.getVipExpire())
                        .plusDays(duration.longValue()));
            }
            isVip = true;
        }
        SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        VipHistory history = new VipHistory()
                .setId(XSUuidUtil.generate())
                .setUserId(userId)
                .setPackageDesc("系统操作")
                .setPackageDuration(duration)
                .setCompleteTime(new Date())
                .setChangeBefore(user.getVipExpire())
                .setChangeAfter(changeAfter)
                .setCreatorId(secStaff.getId())
                .setCreator(secStaff.getNickname());
        history.setCreateTime(new Date());
        vipHistoryDao.save(history);
        userDao.updateVip(user.getId(), isVip ? 1 : 0, changeAfter);
    }

    public XSPageModel<VipHistory> listHistory(User user) {
        return XSPageModel.build(vipHistoryDao.list(user), vipHistoryDao.count(user));
    }
}
