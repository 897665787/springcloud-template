package com.company.tool.banner;

import com.company.framework.context.SpringContextUtil;
import com.company.framework.util.Utils;
import com.company.tool.api.enums.BannerEnum;
import com.company.tool.banner.dto.BannerCanShow;
import com.company.tool.entity.Banner;
import com.company.tool.entity.BannerCondition;
import com.company.tool.service.market.BannerConditionService;
import com.company.tool.service.market.BannerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BannerShowService {
	private final BannerService bannerService;
	private final BannerConditionService bannerConditionService;

	/**
	 * <pre>
	 * 轮播图列表
	 * </pre>
	 *
	 * @param runtimeAttach
	 * @return
	 */
	public List<BannerCanShow> list(Map<String, String> runtimeAttach) {
		LocalDateTime now = LocalDateTime.now();

		// 查询条件符合的轮播图，排序priority desc,id desc
		List<Banner> bannerList = bannerService.selectValidOrderby(BannerEnum.Status.ON, now);
		if (CollectionUtils.isEmpty(bannerList)) {
			return Collections.emptyList();
		}

		Set<Integer> bannerIdSet = bannerList.stream().map(Banner::getId).collect(Collectors.toSet());
		List<BannerCondition> bannerConditionBatchList = bannerConditionService.selectByBannerIds(bannerIdSet);
		Map<Integer, List<BannerCondition>> bannerIdConditionMap = bannerConditionBatchList.stream()
				.collect(Collectors.groupingBy(BannerCondition::getBannerId));

		// 可显示列表
		List<Banner> bannerListCanShow = Lists.newArrayList();
		for (Banner banner : bannerList) {
			List<BannerCondition> bannerConditionList = bannerIdConditionMap.get(banner.getId());
			Boolean canShow = this.canShow(banner, bannerConditionList, runtimeAttach);
			if (!canShow) {// 不可显示的轮播图直接跳过
				continue;
			}
			bannerListCanShow.add(banner);
		}

		if (CollectionUtils.isEmpty(bannerListCanShow)) {
			return Collections.emptyList();
		}

		/* ============定义金刚位配置中的参数============ */
		Map<String, String> configParams = Maps.newHashMap();
		String appUserId = runtimeAttach.get("appUserId");
		configParams.put("{appUserId}", appUserId);

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
		/* ============定义金刚位配置中的参数============ */

		List<BannerCanShow> bannerCanShowList = Lists.newArrayList();
		for (Banner banner : bannerListCanShow) {
			BannerCanShow bannerCanShow = new BannerCanShow();

			bannerCanShow.setTitle(banner.getTitle());
			bannerCanShow.setImage(banner.getImage());
			bannerCanShow.setType(BannerEnum.Type.of(banner.getType()));
			bannerCanShow.setValue(Utils.replaceConfigParams(banner.getValue(), configParams));

			bannerCanShowList.add(bannerCanShow);
		}

		return bannerCanShowList;
	}

	private boolean canShow(Banner banner, List<BannerCondition> bannerConditionList,
			Map<String, String> runtimeAttach) {

		if (CollectionUtils.isEmpty(bannerConditionList)) {
			log.warn("轮播图{}展示条件未配置", banner.getId());
			return false;
		}

		/*
		 * 并发执行条件判断，任意1个匹配false则返回false，否则返回true
		 */
		List<Supplier<Boolean>> supplierList = bannerConditionList.stream().map(v -> {
			Supplier<Boolean> supplier = () -> {
				String beanName = v.getShowCondition();
				BannerShowCondition condition = SpringContextUtil.getBean(beanName, BannerShowCondition.class);
				if (condition == null) {
					log.warn("展示条件未配置:{}", beanName);
					return false;
				}

				ShowParam showParam = ShowParam.builder().bannerId(banner.getId()).runtimeAttach(runtimeAttach)
						.showConditionValue(v.getShowConditionValue()).build();

				boolean canShow = false;
				try {
					canShow = condition.canShow(showParam);
				} catch (Exception e) {
					// 异常情况下不显示
					log.error("canShow error", e);
				}
				return canShow;
			};
			return supplier;
		}).collect(Collectors.toList());

		// 任意1个匹配false则返回false，否则返回true
		Boolean result = Utils.anyMatch(supplierList, v -> false == v, true);
		return result;
	}
}
