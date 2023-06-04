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
import com.company.admin.entity.marketing.HotWord;
import com.company.admin.service.marketing.HotWordService;
import com.company.common.api.Result;

@Controller
public class HotWordController {
	@Autowired
	private HotWordService hotWordService;

	@RequestMapping(value = "/admin/marketing/hotWord/index", method = RequestMethod.GET)
	public String index(Model model, HotWord hotWord, String createTimeStart, String createTimeEnd,
			String updateTimeStart, String updateTimeEnd) {
		model.addAttribute("search", hotWord);
		model.addAttribute("createTimeStart", createTimeStart);
		model.addAttribute("createTimeEnd", createTimeEnd);
		model.addAttribute("updateTimeStart", updateTimeStart);
		model.addAttribute("updateTimeEnd", updateTimeEnd);
		
		Wrapper<HotWord> searchWrapper = toSearchWrapper(hotWord, createTimeStart, createTimeEnd, updateTimeStart,
				updateTimeEnd);
		Page<HotWord> pageResult = hotWordService.selectPage(SelectPageUtil.page(), searchWrapper);
		
		model.addAttribute("pageModel", XSPageModel.build(pageResult.getRecords(), pageResult.getTotal()));
		
		return "marketing/hotWord";
	}

	@RequestMapping(value = "/admin/marketing/hotWord/create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		return "marketing/hotWordCreate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/hotWord/save", method = RequestMethod.POST)
	public Result<Void> save(@Validated(HotWord.Save.class) HotWord hotWord) {
		hotWordService.insert(hotWord);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/hotWord/update", method = RequestMethod.GET)
	public String update(Model model, HttpServletRequest request, HotWord hotWord) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("hotWord", hotWordService.selectById(hotWord.getId()));
		return "marketing/hotWordUpdate";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/hotWord/update", method = RequestMethod.POST)
	public Result<Void> update(@Validated(HotWord.Update.class) HotWord hotWord) {
		hotWordService.updateById(hotWord);
		return Result.success();
	}

	@RequestMapping(value = "/admin/marketing/hotWord/detail", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, HotWord hotWord) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("hotWord", hotWordService.selectById(hotWord.getId()));
		return "marketing/hotWordDetail";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/hotWord/get", method = RequestMethod.POST)
	public Result<HotWord> get(HotWord hotWord) {
		return Result.success(hotWordService.selectById(hotWord.getId()));
	}

	@ResponseBody
	@RequestMapping(value = "/admin/marketing/hotWord/remove", method = RequestMethod.POST)
	public Result<Void> remove(HotWord hotWord) {
		hotWordService.deleteById(hotWord.getId());
		return Result.success();
	}
    
    private Wrapper<HotWord> toSearchWrapper(HotWord hotWord, String createTimeStart,
			String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
    	Wrapper<HotWord> wrapper = new EntityWrapper<HotWord>();
		if (StringUtils.isNotBlank(hotWord.getName())) {
			wrapper.like("name", hotWord.getName());
		}
		if (hotWord.getStatus() != null) {
			wrapper.eq("status", hotWord.getStatus());
		}
		if (hotWord.getSeq() != null) {
			wrapper.eq("seq", hotWord.getSeq());
		}
		if (hotWord.getCreateTime() != null) {
			wrapper.eq("create_time", hotWord.getCreateTime());
		}
		if (hotWord.getUpdateTime() != null) {
			wrapper.eq("update_time", hotWord.getUpdateTime());
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