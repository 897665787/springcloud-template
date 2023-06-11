package com.company.admin.controller.marketing;

import java.util.List;
import java.util.Map;

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
import com.company.admin.entity.marketing.CouponTemplate;
import com.company.admin.entity.marketing.CouponTemplateCondition;
import com.company.admin.entity.marketing.CouponUseCondition;
import com.company.admin.service.marketing.CouponTemplateConditionService;
import com.company.admin.service.marketing.CouponTemplateService;
import com.company.admin.service.marketing.CouponUseConditionService;
import com.company.common.api.Result;
import com.google.common.collect.Maps;

@Controller
public class CouponTemplateController {
	@Autowired
	private CouponTemplateService couponTemplateService;
	@Autowired
	private CouponTemplateConditionService couponTemplateConditionService;
	@Autowired
	private CouponUseConditionService couponUseConditionService;

	@RequestMapping(value = "/admin/marketing/couponTemplate/index", method = RequestMethod.GET)
	public String index(Model model, CouponTemplate couponTemplate, String createTimeStart, String createTimeEnd,
			String updateTimeStart, String updateTimeEnd) {
		model.addAttribute("search", couponTemplate);
		model.addAttribute("createTimeStart", createTimeStart);
		model.addAttribute("createTimeEnd", createTimeEnd);
		model.addAttribute("updateTimeStart", updateTimeStart);
		model.addAttribute("updateTimeEnd", updateTimeEnd);
		
		Wrapper<CouponTemplate> searchWrapper = toSearchWrapper(couponTemplate, createTimeStart, createTimeEnd, updateTimeStart,
				updateTimeEnd);
		Page<CouponTemplate> pageResult = couponTemplateService.selectPage(SelectPageUtil.page(), searchWrapper);
		
		model.addAttribute("pageModel", XSPageModel.build(pageResult.getRecords(), pageResult.getTotal()));
		
		Wrapper<CouponUseCondition> couponUseConditionWrapper = new EntityWrapper<CouponUseCondition>();
		couponUseConditionWrapper.orderBy("id", false);
		List<CouponUseCondition> couponUseConditionList = couponUseConditionService.selectList(couponUseConditionWrapper);
		model.addAttribute("couponUseConditionList", couponUseConditionList);
		
		return "marketing/couponTemplate";
	}

	@RequestMapping(value = "/admin/marketing/couponTemplate/create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		return "marketing/couponTemplateCreate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplate/save", method = RequestMethod.POST)
	public Result<Void> save(@Validated(CouponTemplate.Save.class) CouponTemplate couponTemplate) {
		couponTemplateService.insert(couponTemplate);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/couponTemplate/update", method = RequestMethod.GET)
	public String update(Model model, HttpServletRequest request, CouponTemplate couponTemplate) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("couponTemplate", couponTemplateService.selectById(couponTemplate.getId()));
		return "marketing/couponTemplateUpdate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplate/update", method = RequestMethod.POST)
	public Result<Void> update(@Validated(CouponTemplate.Update.class) CouponTemplate couponTemplate) {
		couponTemplateService.updateById(couponTemplate);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/couponTemplate/detail", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, CouponTemplate couponTemplate) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("couponTemplate", couponTemplateService.selectById(couponTemplate.getId()));
		return "marketing/couponTemplateDetail";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplate/get", method = RequestMethod.POST)
	public Result<Map<String, Object>> get(CouponTemplate couponTemplate) {
		Map<String, Object> result = Maps.newHashMap();
		CouponTemplate couponTemplateDB = couponTemplateService.selectById(couponTemplate.getId());
		
		result.put("couponTemplate", couponTemplateDB);
		
		Wrapper<CouponTemplateCondition> couponTemplateConditionWrapper = new EntityWrapper<CouponTemplateCondition>();
		couponTemplateConditionWrapper.eq("coupon_template_id", couponTemplate.getId());
		couponTemplateConditionWrapper.orderBy("id", true);
		List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateConditionService.selectList(couponTemplateConditionWrapper);
		result.put("couponTemplateConditionList", couponTemplateConditionList);
		return Result.success(result);
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/couponTemplate/remove", method = RequestMethod.POST)
	public Result<Void> remove(CouponTemplate couponTemplate) {
		couponTemplateService.deleteById(couponTemplate.getId());
		return Result.success();
	}
    
    private Wrapper<CouponTemplate> toSearchWrapper(CouponTemplate couponTemplate, String createTimeStart,
			String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
    	Wrapper<CouponTemplate> wrapper = new EntityWrapper<CouponTemplate>();
		if (StringUtils.isNotBlank(couponTemplate.getName())) {
			wrapper.like("name", couponTemplate.getName());
		}
		if (couponTemplate.getMaxAmount() != null) {
			wrapper.eq("max_amount", couponTemplate.getMaxAmount());
		}
		if (couponTemplate.getDiscount() != null) {
			wrapper.eq("discount", couponTemplate.getDiscount());
		}
		if (couponTemplate.getConditionAmount() != null) {
			wrapper.eq("condition_amount", couponTemplate.getConditionAmount());
		}
		if (couponTemplate.getBeginTime() != null) {
			wrapper.eq("begin_time", couponTemplate.getBeginTime());
		}
		if (couponTemplate.getEndTime() != null) {
			wrapper.eq("end_time", couponTemplate.getEndTime());
		}
		if (StringUtils.isNotBlank(couponTemplate.getPeriodType())) {
			wrapper.like("period_type", couponTemplate.getPeriodType());
		}
		if (couponTemplate.getTotalNum() != null) {
			wrapper.eq("total_num", couponTemplate.getTotalNum());
		}
		if (couponTemplate.getLeftNum() != null) {
			wrapper.eq("left_num", couponTemplate.getLeftNum());
		}
		if (couponTemplate.getLimitNum() != null) {
			wrapper.eq("limit_num", couponTemplate.getLimitNum());
		}
		if (StringUtils.isNotBlank(couponTemplate.getUseDescription())) {
			wrapper.like("use_description", couponTemplate.getUseDescription());
		}
		if (StringUtils.isNotBlank(couponTemplate.getRemark())) {
			wrapper.like("remark", couponTemplate.getRemark());
		}
		if (couponTemplate.getCreateTime() != null) {
			wrapper.eq("create_time", couponTemplate.getCreateTime());
		}
		if (couponTemplate.getUpdateTime() != null) {
			wrapper.eq("update_time", couponTemplate.getUpdateTime());
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