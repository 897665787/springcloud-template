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
import com.company.admin.entity.marketing.CouponTemplateCondition;
import com.company.admin.service.marketing.CouponTemplateConditionService;
import com.company.common.api.Result;

@Controller
public class CouponTemplateConditionController {
	@Autowired
	private CouponTemplateConditionService couponTemplateConditionService;

	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/index", method = RequestMethod.GET)
	public String index(Model model, CouponTemplateCondition couponTemplateCondition, String createTimeStart, String createTimeEnd,
			String updateTimeStart, String updateTimeEnd) {
		model.addAttribute("search", couponTemplateCondition);
		model.addAttribute("createTimeStart", createTimeStart);
		model.addAttribute("createTimeEnd", createTimeEnd);
		model.addAttribute("updateTimeStart", updateTimeStart);
		model.addAttribute("updateTimeEnd", updateTimeEnd);
		
		Wrapper<CouponTemplateCondition> searchWrapper = toSearchWrapper(couponTemplateCondition, createTimeStart, createTimeEnd, updateTimeStart,
				updateTimeEnd);
		Page<CouponTemplateCondition> pageResult = couponTemplateConditionService.selectPage(SelectPageUtil.page(), searchWrapper);
		
		model.addAttribute("pageModel", XSPageModel.build(pageResult.getRecords(), pageResult.getTotal()));
		
		return "marketing/couponTemplateCondition";
	}

	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		return "marketing/couponTemplateConditionCreate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/save", method = RequestMethod.POST)
	public Result<Void> save(@Validated(CouponTemplateCondition.Save.class) CouponTemplateCondition couponTemplateCondition) {
		couponTemplateConditionService.insert(couponTemplateCondition);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/update", method = RequestMethod.GET)
	public String update(Model model, HttpServletRequest request, CouponTemplateCondition couponTemplateCondition) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("couponTemplateCondition", couponTemplateConditionService.selectById(couponTemplateCondition.getId()));
		return "marketing/couponTemplateConditionUpdate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/update", method = RequestMethod.POST)
	public Result<Void> update(@Validated(CouponTemplateCondition.Update.class) CouponTemplateCondition couponTemplateCondition) {
		couponTemplateConditionService.updateById(couponTemplateCondition);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/detail", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, CouponTemplateCondition couponTemplateCondition) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("couponTemplateCondition", couponTemplateConditionService.selectById(couponTemplateCondition.getId()));
		return "marketing/couponTemplateConditionDetail";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/get", method = RequestMethod.POST)
	public Result<CouponTemplateCondition> get(CouponTemplateCondition couponTemplateCondition) {
		return Result.success(couponTemplateConditionService.selectById(couponTemplateCondition.getId()));
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplateCondition/remove", method = RequestMethod.POST)
	public Result<Void> remove(CouponTemplateCondition couponTemplateCondition) {
		couponTemplateConditionService.deleteById(couponTemplateCondition.getId());
		return Result.success();
	}
    
    private Wrapper<CouponTemplateCondition> toSearchWrapper(CouponTemplateCondition couponTemplateCondition, String createTimeStart,
			String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
    	Wrapper<CouponTemplateCondition> wrapper = new EntityWrapper<CouponTemplateCondition>();
		if (couponTemplateCondition.getCouponTemplateId() != null) {
			wrapper.eq("coupon_template_id", couponTemplateCondition.getCouponTemplateId());
		}
		if (StringUtils.isNotBlank(couponTemplateCondition.getUseCondition())) {
			wrapper.like("use_condition", couponTemplateCondition.getUseCondition());
		}
		if (StringUtils.isNotBlank(couponTemplateCondition.getUseConditionValue())) {
			wrapper.like("use_condition_value", couponTemplateCondition.getUseConditionValue());
		}
		if (StringUtils.isNotBlank(couponTemplateCondition.getRemark())) {
			wrapper.like("remark", couponTemplateCondition.getRemark());
		}
		if (couponTemplateCondition.getCreateTime() != null) {
			wrapper.eq("create_time", couponTemplateCondition.getCreateTime());
		}
		if (couponTemplateCondition.getUpdateTime() != null) {
			wrapper.eq("update_time", couponTemplateCondition.getUpdateTime());
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