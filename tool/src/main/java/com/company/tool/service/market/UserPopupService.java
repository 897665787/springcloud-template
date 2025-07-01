package com.company.tool.service.market;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.util.JsonUtil;
import com.company.framework.util.Utils;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.entity.UserPopup;
import com.company.tool.mapper.market.UserPopupMapper;
import com.company.tool.popup.dto.PopButton;
import com.company.tool.popup.dto.PopImage;

@Component
public class UserPopupService extends ServiceImpl<UserPopupMapper, UserPopup> implements IService<UserPopup> {

	@Autowired
	private PopupLogService popupLogService;

	public UserPopup selectByPopupLog(Integer userId, PopupEnum.LogBusinessType businessType,
			PopupEnum.Status status, LocalDateTime time) {
		return baseMapper.selectByPopupLog(userId, businessType, status, time);
	}

	/**
	 * 创建用户弹窗
	 */
	public void createUserPopup(Integer userId, LocalDateTime beginTime, LocalDateTime endTime, Integer priority,
			String title, String text, PopImage bgImg, PopButton closeBtn) {
		UserPopup userPopup = new UserPopup();
		userPopup.setUserId(userId);
		userPopup.setBeginTime(beginTime);
		userPopup.setEndTime(endTime);
		userPopup.setStatus(PopupEnum.Status.ON.getCode());
		userPopup.setPriority(priority);

		userPopup.setTitle(title);
		userPopup.setText(text);

		userPopup.setBgImg(JsonUtil.toJsonString(bgImg));
		userPopup.setCloseBtn(JsonUtil.toJsonString(closeBtn));

		baseMapper.insert(userPopup);
	}

	/**
	 * 取消用户弹窗（如果已经弹过，则不处理；未弹过，则下架该弹窗）
	 *
	 * <pre>
	 * 使用场景：根据用户的步骤来弹窗，可能某个弹窗已经到下一步了，但是上一步的弹窗还没弹，再弹上一步的弹窗已经没有意义了
	 * </pre>
	 */
	public void cancelUserPopup(Integer userId, String title, String remark) {
		List<UserPopup> userPopupList = baseMapper.selectByUserIdTitle(userId, title);
		for (UserPopup userPopupDb : userPopupList) {
			if (PopupEnum.Status.of(userPopupDb.getStatus()) != PopupEnum.Status.ON) {
				// 弹窗已下架
				return;
			}

			LocalDateTime lastPopupTime = popupLogService.lastPopupTimeByBusiness(PopupEnum.LogBusinessType.USER_POPUP,
					userPopupDb.getId(), userId, null);

			if (lastPopupTime != null) {
				// 已经弹过了，不处理
				return;
			}

			UserPopup userPopup4Update = new UserPopup();
			userPopup4Update.setId(userPopupDb.getId());

			userPopup4Update.setStatus(PopupEnum.Status.OFF.getCode());
			userPopup4Update.setRemark(Utils.rightRemark(userPopupDb.getRemark(), remark));

			baseMapper.updateById(userPopup4Update);
		}
	}
}
