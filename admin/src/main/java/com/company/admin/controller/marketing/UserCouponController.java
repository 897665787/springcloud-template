package com.company.admin.controller.marketing;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.company.admin.controller.util.SelectPageUtil;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.marketing.UserCoupon;
import com.company.admin.service.marketing.UserCouponService;
import com.company.common.api.Result;

@Controller
public class UserCouponController {
	@Autowired
	private UserCouponService userCouponService;

	@RequestMapping(value = "/admin/marketing/userCoupon/index", method = RequestMethod.GET)
	public String index(Model model, UserCoupon userCoupon, String createTimeStart, String createTimeEnd,
			String updateTimeStart, String updateTimeEnd) {
		model.addAttribute("search", userCoupon);
		model.addAttribute("createTimeStart", createTimeStart);
		model.addAttribute("createTimeEnd", createTimeEnd);
		model.addAttribute("updateTimeStart", updateTimeStart);
		model.addAttribute("updateTimeEnd", updateTimeEnd);
		
		Wrapper<UserCoupon> searchWrapper = toSearchWrapper(userCoupon, createTimeStart, createTimeEnd, updateTimeStart,
				updateTimeEnd);
		Page<UserCoupon> pageResult = userCouponService.selectPage(SelectPageUtil.page(), searchWrapper);
		
		model.addAttribute("pageModel", XSPageModel.build(pageResult.getRecords(), pageResult.getTotal()));
		
		return "marketing/userCoupon";
	}

	@RequestMapping(value = "/admin/marketing/userCoupon/create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		return "marketing/userCouponCreate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/userCoupon/save", method = RequestMethod.POST)
	public Result<Void> save(@Validated(UserCoupon.Save.class) UserCoupon userCoupon) {
		userCouponService.insert(userCoupon);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/userCoupon/update", method = RequestMethod.GET)
	public String update(Model model, HttpServletRequest request, UserCoupon userCoupon) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("userCoupon", userCouponService.selectById(userCoupon.getId()));
		return "marketing/userCouponUpdate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/userCoupon/update", method = RequestMethod.POST)
	public Result<Void> update(@Validated(UserCoupon.Update.class) UserCoupon userCoupon) {
		userCouponService.updateById(userCoupon);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/userCoupon/detail", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, UserCoupon userCoupon) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("userCoupon", userCouponService.selectById(userCoupon.getId()));
		return "marketing/userCouponDetail";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/userCoupon/get", method = RequestMethod.POST)
	public Result<UserCoupon> get(UserCoupon userCoupon) {
		return Result.success(userCouponService.selectById(userCoupon.getId()));
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/userCoupon/remove", method = RequestMethod.POST)
	public Result<Void> remove(UserCoupon userCoupon) {
		userCouponService.deleteById(userCoupon.getId());
		return Result.success();
	}
    
    private Wrapper<UserCoupon> toSearchWrapper(UserCoupon userCoupon, String createTimeStart,
			String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
    	Wrapper<UserCoupon> wrapper = new EntityWrapper<UserCoupon>();
		if (userCoupon.getUserId() != null) {
			wrapper.eq("user_id", userCoupon.getUserId());
		}
		if (userCoupon.getCouponTemplateId() != null) {
			wrapper.eq("coupon_template_id", userCoupon.getCouponTemplateId());
		}
		if (StringUtils.isNotBlank(userCoupon.getName())) {
			wrapper.like("name", userCoupon.getName());
		}
		if (userCoupon.getMaxAmount() != null) {
			wrapper.eq("max_amount", userCoupon.getMaxAmount());
		}
		if (userCoupon.getDiscount() != null) {
			wrapper.eq("discount", userCoupon.getDiscount());
		}
		if (userCoupon.getConditionAmount() != null) {
			wrapper.eq("condition_amount", userCoupon.getConditionAmount());
		}
		if (userCoupon.getBeginTime() != null) {
			wrapper.eq("begin_time", userCoupon.getBeginTime());
		}
		if (userCoupon.getEndTime() != null) {
			wrapper.eq("end_time", userCoupon.getEndTime());
		}
		if (StringUtils.isNotBlank(userCoupon.getStatus())) {
			wrapper.like("status", userCoupon.getStatus());
		}
		if (userCoupon.getMaxNum() != null) {
			wrapper.eq("max_num", userCoupon.getMaxNum());
		}
		if (StringUtils.isNotBlank(userCoupon.getRemark())) {
			wrapper.like("remark", userCoupon.getRemark());
		}
		if (userCoupon.getCreateTime() != null) {
			wrapper.eq("create_time", userCoupon.getCreateTime());
		}
		if (userCoupon.getUpdateTime() != null) {
			wrapper.eq("update_time", userCoupon.getUpdateTime());
		}
		if (StringUtils.isNotBlank(createTimeStart)) {
			wrapper.ge("create_time", createTimeStart + " 00:00:00");
		}
		if (StringUtils.isNotBlank(createTimeEnd)) {
			wrapper.le("create_time", createTimeEnd + " 23:59:59");
		}
		if (StringUtils.isNotBlank(updateTimeStart)) {
			wrapper.ge("update_time", updateTimeStart + " 00:00:00");
		}
		if (StringUtils.isNotBlank(updateTimeEnd)) {
			wrapper.le("update_time", updateTimeEnd + " 23:59:59");
		}
		return wrapper;
	}
}