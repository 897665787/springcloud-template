package com.company.tool.popup;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.company.framework.trace.TraceManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.framework.util.Utils;
import com.company.framework.context.SpringContextUtil;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.entity.Popup;
import com.company.tool.entity.PopupCondition;
import com.company.tool.entity.PopupLog;
import com.company.tool.entity.UserPopup;
import com.company.tool.popup.dto.PopButton;
import com.company.tool.popup.dto.PopImage;
import com.company.tool.popup.dto.PopupCanPop;
import com.company.tool.service.market.PopupConditionService;
import com.company.tool.service.market.PopupLogService;
import com.company.tool.service.market.PopupService;
import com.company.tool.service.market.UserPopupService;
import com.google.common.collect.Maps;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PopService {

	@Autowired
	private PopupService popupService;
	@Autowired
	private PopupConditionService popupConditionService;
	@Autowired
	private UserPopupService userPopupService;
	@Autowired
	private PopupLogService popupLogService;
	@Autowired
	private TraceManager traceManager;

	@Value("${popup.minPopInterval.seconds:0}")
	private Integer minPopIntervalSeconds;// 弹窗最小时间间隔(s)

	/**
	 * <pre>
	 * 用户最优的弹窗
	 * </pre>
	 *
	 * @param userId
	 *            用户ID
	 * @param deviceid
	 *            设备号(小程序是openid，APP是设备ID)
	 * @param runtimeAttach
	 * @return
	 */
	public PopupCanPop bestPop(Integer userId, String deviceid, Map<String, String> runtimeAttach) {
		// userId和deviceid必须有1个有值
		if ((userId == null || userId < 1) && StringUtils.isBlank(deviceid)) {
			log.warn("userId和deviceid必须有1个有值,userId:{},deviceid:{}", userId, deviceid);
			return null;
		}

		LocalDateTime now = LocalDateTime.now();

		if (minPopIntervalSeconds > 0) {
			// 查询用户最近1次弹窗时间，没到时间不弹
			LocalDateTime lastPopupTime = popupLogService.lastPopupTime(userId, deviceid);
			if (lastPopupTime != null) {
				long diffSeconds = LocalDateTimeUtil.between(lastPopupTime, now, ChronoUnit.SECONDS);
				if (diffSeconds < minPopIntervalSeconds) {// n秒内不重复弹窗
					log.info("{}秒内不重复弹窗,diffSeconds:{}", minPopIntervalSeconds, diffSeconds);
					return null;
				}
			}
		}

		// 查询公共弹窗
		List<Popup> popupList = popupService.selectByPopupLog(PopupEnum.Status.ON, now);

		Set<Integer> popupIdSet = popupList.stream().map(Popup::getId).collect(Collectors.toSet());

		List<PopupCondition> popupConditionBatchList = popupConditionService.selectByPopupIds(popupIdSet);

		Map<Integer, List<PopupCondition>> popupIdConditionMap = popupConditionBatchList.stream()
				.collect(Collectors.groupingBy(PopupCondition::getPopupId));

		Popup bestPopup = null;
		for (Popup popup : popupList) {
			List<PopupCondition> popupConditionList = popupIdConditionMap.get(popup.getId());
			Boolean canPop = this.canPop(popup, popupConditionList, userId, deviceid, runtimeAttach);
			if (canPop) {
				bestPopup = popup;
				break;
			}
		}

		PopupCanPop bestPopupCanPop = null;
		if (bestPopup != null) {
			/* ============定义弹窗配置中的参数============ */
			Map<String, String> configParams = Maps.newHashMap();
			configParams.put("{userId}", String.valueOf(userId));
			configParams.put("{deviceid}", deviceid);

			String longitude = runtimeAttach.get("longitude");
			configParams.put("{longitude}", longitude);
			String latitude = runtimeAttach.get("latitude");
			configParams.put("{latitude}", latitude);

			String cityCode = runtimeAttach.get("cityCode");
			if (StringUtils.isBlank(cityCode)) {
				cityCode = "440300";
			}
			configParams.put("{cityCode}", cityCode);

			String token = runtimeAttach.get("token");
			configParams.put("{token}", token);
			/* ============定义弹窗配置中的参数============ */

			// 自定义参数替换（可用于复杂参数的生成）
			Map<String, ReplaceParam> replaceParamBeans = SpringContextUtil.getBeansOfType(ReplaceParam.class);
			Set<Entry<String, ReplaceParam>> entrySet = replaceParamBeans.entrySet();
			for (Entry<String, ReplaceParam> entry : entrySet) {
				String key = entry.getKey();
				boolean anyContains = Utils.anyContains(key, bestPopup.getTitle(), bestPopup.getText(),
						bestPopup.getBgImg(), bestPopup.getCloseBtn());
				if (anyContains) {
					ReplaceParam replaceParam = entry.getValue();
					String replace = replaceParam.replace(userId);
					configParams.put(key, replace);
				}
			}

			bestPopupCanPop = this.buildPopupCanPop(bestPopup, configParams);
		}

		// 查询个人弹窗
		UserPopup userPopup = userPopupService.selectByPopupLog(userId, PopupEnum.LogBusinessType.USER_POPUP,
				PopupEnum.Status.ON, now);
		if (userPopup != null
				&& (bestPopupCanPop == null || userPopup.getPriority() >= bestPopupCanPop.getPriority())) {
			bestPopupCanPop = this.buildPopupCanPop(userPopup);
		}

		// 记录已经弹窗
		if (bestPopupCanPop != null) {
			PopupLog popupLog = new PopupLog();
			popupLog.setBusinessType(bestPopupCanPop.getBusinessType().getCode());
			popupLog.setBusinessId(bestPopupCanPop.getBusinessId());
			popupLog.setUserId(userId);
			popupLog.setDeviceid(deviceid);
			popupLogService.save(popupLog);

			bestPopupCanPop.setPopupLogId(popupLog.getId());
		}
		return bestPopupCanPop;
	}

	private boolean canPop(Popup popup, List<PopupCondition> popupConditionList, Integer userId, String deviceid,
			Map<String, String> runtimeAttach) {

		if (CollectionUtils.isEmpty(popupConditionList)) {
			log.warn("弹窗模板{}弹窗条件未配置", popup.getId());
			return false;
		}

		/*
		 * 并发执行条件判断，任意1个匹配false则返回false，否则返回true
		 */
		String traceId = traceManager.get();
		List<Supplier<Boolean>> supplierList = popupConditionList.stream().map(v -> {
			Supplier<Boolean> supplier = () -> {
				String subTraceId = traceManager.get();
				if (subTraceId == null) {
					traceManager.put(traceId);
				}
				String beanName = v.getPopCondition();
				PopCondition condition = SpringContextUtil.getBean(beanName, PopCondition.class);
				if (condition == null) {
					log.warn("弹窗条件未配置:{}", beanName);
					return false;
				}

				PopParam popParam = PopParam.builder().popupId(popup.getId()).userId(userId).deviceid(deviceid)
						.runtimeAttach(runtimeAttach).popConditionValue(v.getPopConditionValue()).build();

				boolean canPop = false;
				try {
					canPop = condition.canPop(popParam);
				} catch (Exception e) {
					// 异常情况下不弹窗
					log.error("canPop error", e);
				}
				if (subTraceId == null) {
					traceManager.remove();
				}
				return canPop;
			};
			return supplier;
		}).collect(Collectors.toList());

		// 任意1个匹配false则返回false，否则返回true
		Boolean result = Utils.anyMatch(supplierList, v -> v == false, true);
		return result;
	}

	private PopupCanPop buildPopupCanPop(Popup popup, Map<String, String> configParams) {
		if (popup == null) {
			return null;
		}
		PopupCanPop bestPopupCanPop = new PopupCanPop();
		bestPopupCanPop.setBusinessType(PopupEnum.LogBusinessType.POPUP);
		bestPopupCanPop.setBusinessId(popup.getId());
		bestPopupCanPop.setPriority(popup.getPriority());

		// 其他数据
		bestPopupCanPop.setTitle(Utils.replaceConfigParams(popup.getTitle(), configParams));
		bestPopupCanPop.setText(Utils.replaceConfigParams(popup.getText(), configParams));

		PopImage popImage = JsonUtil.toEntity(popup.getBgImg(), PopImage.class);
		if (popImage != null) {
			popImage.setImgUrl(Utils.replaceConfigParams(popImage.getImgUrl(), configParams));
			popImage.setValue(Utils.replaceConfigParams(popImage.getValue(), configParams));
			bestPopupCanPop.setBgImg(popImage);

			PopImage nextPopImage = popImage.getNext();
			while (nextPopImage != null) {
				nextPopImage.setImgUrl(Utils.replaceConfigParams(nextPopImage.getImgUrl(), configParams));
				nextPopImage.setValue(Utils.replaceConfigParams(nextPopImage.getValue(), configParams));
				nextPopImage = nextPopImage.getNext();
			}
		}

		PopButton closeBtn = JsonUtil.toEntity(popup.getCloseBtn(), PopButton.class);
		if (closeBtn != null) {
			closeBtn.setText(Utils.replaceConfigParams(closeBtn.getText(), configParams));
			closeBtn.setValue(Utils.replaceConfigParams(closeBtn.getValue(), configParams));
			bestPopupCanPop.setCloseBtn(closeBtn);
		}

		return bestPopupCanPop;
	}

	private PopupCanPop buildPopupCanPop(UserPopup userPopup) {
		PopupCanPop bestPopupCanPop = new PopupCanPop();
		bestPopupCanPop.setBusinessType(PopupEnum.LogBusinessType.USER_POPUP);
		bestPopupCanPop.setBusinessId(userPopup.getId());
		bestPopupCanPop.setPriority(userPopup.getPriority());

		// 其他数据
		bestPopupCanPop.setTitle(userPopup.getTitle());
		bestPopupCanPop.setText(userPopup.getText());
		bestPopupCanPop.setBgImg(JsonUtil.toEntity(userPopup.getBgImg(), PopImage.class));
		bestPopupCanPop.setCloseBtn(JsonUtil.toEntity(userPopup.getCloseBtn(), PopButton.class));
		return bestPopupCanPop;
	}
}
