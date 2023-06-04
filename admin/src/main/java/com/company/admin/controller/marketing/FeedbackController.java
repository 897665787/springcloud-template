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
import com.company.admin.entity.marketing.Feedback;
import com.company.admin.service.marketing.FeedbackService;
import com.company.common.api.Result;

@Controller
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;

	@RequestMapping(value = "/admin/marketing/feedback/index", method = RequestMethod.GET)
	public String index(Model model, Feedback feedback, String createTimeStart, String createTimeEnd,
			String updateTimeStart, String updateTimeEnd) {
		model.addAttribute("search", feedback);
		model.addAttribute("createTimeStart", createTimeStart);
		model.addAttribute("createTimeEnd", createTimeEnd);
		model.addAttribute("updateTimeStart", updateTimeStart);
		model.addAttribute("updateTimeEnd", updateTimeEnd);
		
		Wrapper<Feedback> searchWrapper = toSearchWrapper(feedback, createTimeStart, createTimeEnd, updateTimeStart,
				updateTimeEnd);
		Page<Feedback> pageResult = feedbackService.selectPage(SelectPageUtil.page(), searchWrapper);
		
		model.addAttribute("pageModel", XSPageModel.build(pageResult.getRecords(), pageResult.getTotal()));
		
		return "marketing/feedback";
	}

	@RequestMapping(value = "/admin/marketing/feedback/create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		return "marketing/feedbackCreate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/feedback/save", method = RequestMethod.POST)
	public Result<Void> save(@Validated(Feedback.Save.class) Feedback feedback) {
		feedbackService.insert(feedback);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/feedback/update", method = RequestMethod.GET)
	public String update(Model model, HttpServletRequest request, Feedback feedback) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("feedback", feedbackService.selectById(feedback.getId()));
		return "marketing/feedbackUpdate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/feedback/update", method = RequestMethod.POST)
	public Result<Void> update(@Validated(Feedback.Update.class) Feedback feedback) {
		feedbackService.updateById(feedback);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/feedback/detail", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, Feedback feedback) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("feedback", feedbackService.selectById(feedback.getId()));
		return "marketing/feedbackDetail";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/feedback/get", method = RequestMethod.POST)
	public Result<Feedback> get(Feedback feedback) {
		return Result.success(feedbackService.selectById(feedback.getId()));
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/feedback/remove", method = RequestMethod.POST)
	public Result<Void> remove(Feedback feedback) {
		feedbackService.deleteById(feedback.getId());
		return Result.success();
	}
    
    private Wrapper<Feedback> toSearchWrapper(Feedback feedback, String createTimeStart,
			String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
    	Wrapper<Feedback> wrapper = new EntityWrapper<Feedback>();
		if (StringUtils.isNotBlank(feedback.getName())) {
			wrapper.like("name", feedback.getName());
		}
		if (StringUtils.isNotBlank(feedback.getMobile())) {
			wrapper.like("mobile", feedback.getMobile());
		}
		if (StringUtils.isNotBlank(feedback.getTitle())) {
			wrapper.like("title", feedback.getTitle());
		}
		if (StringUtils.isNotBlank(feedback.getContent())) {
			wrapper.like("content", feedback.getContent());
		}
		if (feedback.getStatus() != null) {
			wrapper.eq("status", feedback.getStatus());
		}
		if (feedback.getCreateTime() != null) {
			wrapper.eq("create_time", feedback.getCreateTime());
		}
		if (feedback.getUpdateTime() != null) {
			wrapper.eq("update_time", feedback.getUpdateTime());
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