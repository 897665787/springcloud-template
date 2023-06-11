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
import com.company.admin.entity.marketing.CouponUseCondition;
import com.company.admin.service.marketing.CouponUseConditionService;
import com.company.common.api.Result;

@Controller
public class CouponUseConditionController {
	@Autowired
	private CouponUseConditionService couponUseConditionService;

	@RequestMapping(value = "/admin/marketing/couponUseCondition/index", method = RequestMethod.GET)
	public String index(Model model, CouponUseCondition couponUseCondition, String createTimeStart, String createTimeEnd,
			String updateTimeStart, String updateTimeEnd) {
		model.addAttribute("search", couponUseCondition);
		model.addAttribute("createTimeStart", createTimeStart);
		model.addAttribute("createTimeEnd", createTimeEnd);
		model.addAttribute("updateTimeStart", updateTimeStart);
		model.addAttribute("updateTimeEnd", updateTimeEnd);
		
		Wrapper<CouponUseCondition> searchWrapper = toSearchWrapper(couponUseCondition, createTimeStart, createTimeEnd, updateTimeStart,
				updateTimeEnd);
		Page<CouponUseCondition> pageResult = couponUseConditionService.selectPage(SelectPageUtil.page(), searchWrapper);
		
		model.addAttribute("pageModel", XSPageModel.build(pageResult.getRecords(), pageResult.getTotal()));
		
		return "marketing/couponUseCondition";
	}

	@RequestMapping(value = "/admin/marketing/couponUseCondition/create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		return "marketing/couponUseConditionCreate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponUseCondition/save", method = RequestMethod.POST)
	public Result<Void> save(@Validated(CouponUseCondition.Save.class) CouponUseCondition couponUseCondition) {
		couponUseConditionService.insert(couponUseCondition);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/couponUseCondition/update", method = RequestMethod.GET)
	public String update(Model model, HttpServletRequest request, CouponUseCondition couponUseCondition) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("couponUseCondition", couponUseConditionService.selectById(couponUseCondition.getId()));
		return "marketing/couponUseConditionUpdate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponUseCondition/update", method = RequestMethod.POST)
	public Result<Void> update(@Validated(CouponUseCondition.Update.class) CouponUseCondition couponUseCondition) {
		couponUseConditionService.updateById(couponUseCondition);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/couponUseCondition/detail", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, CouponUseCondition couponUseCondition) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("couponUseCondition", couponUseConditionService.selectById(couponUseCondition.getId()));
		return "marketing/couponUseConditionDetail";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponUseCondition/get", method = RequestMethod.POST)
	public Result<CouponUseCondition> get(CouponUseCondition couponUseCondition) {
		return Result.success(couponUseConditionService.selectById(couponUseCondition.getId()));
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponUseCondition/remove", method = RequestMethod.POST)
	public Result<Void> remove(CouponUseCondition couponUseCondition) {
		couponUseConditionService.deleteById(couponUseCondition.getId());
		return Result.success();
	}
    
    private Wrapper<CouponUseCondition> toSearchWrapper(CouponUseCondition couponUseCondition, String createTimeStart,
			String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
    	Wrapper<CouponUseCondition> wrapper = new EntityWrapper<CouponUseCondition>();
		if (StringUtils.isNotBlank(couponUseCondition.getBeanName())) {
			wrapper.like("bean_name", couponUseCondition.getBeanName());
		}
		if (StringUtils.isNotBlank(couponUseCondition.getDescrpition())) {
			wrapper.like("descrpition", couponUseCondition.getDescrpition());
		}
		if (StringUtils.isNotBlank(couponUseCondition.getRemark())) {
			wrapper.like("remark", couponUseCondition.getRemark());
		}
		if (couponUseCondition.getCreateTime() != null) {
			wrapper.eq("create_time", couponUseCondition.getCreateTime());
		}
		if (couponUseCondition.getUpdateTime() != null) {
			wrapper.eq("update_time", couponUseCondition.getUpdateTime());
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
		wrapper.orderBy("id", false);
		return wrapper;
	}
}