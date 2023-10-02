package com.company.tool.popup.param;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.tool.popup.ReplaceParam;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 新人券包
 */
@Component("{newUserCoupon}")
public class NewUserCouponReplaceParam implements ReplaceParam {

	@Override
	public String replace(Integer userId) {
		// 查询新人券模板
		List<String> couponTemplateList = Lists.newArrayList();

		List<NewUserCoupon> newUserCouponList = couponTemplateList.stream().map(v -> {
			NewUserCoupon coupon = new NewUserCoupon();
//			coupon.setCouponName(v.getUseTypeDesc());
//			coupon.setCouponAmount(v.getReduceAmount().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
//			coupon.setUseRule(v.getTemplateDescription());

//			Integer effectDay = v.getEffectDays();
//			DateTime useEndTime = DateUtil.offsetDay(new Date(), effectDay);
//			String useEndTimeTip = "有效期至" + DateUtil.formatDateTime(useEndTime);
//			coupon.setUseEndTimeTip(useEndTimeTip);
			return coupon;
		}).collect(Collectors.toList());

		return JsonUtil.toJsonString(newUserCouponList);
	}

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	private static class NewUserCoupon {
		String couponName;
		String useEndTimeTip;
		String couponAmount;
		String useRule;
	}
}