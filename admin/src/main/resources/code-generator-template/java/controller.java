package com.company.admin.controller.{module};

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
import com.company.admin.entity.{module}.{ModelName};
import com.company.admin.service.{module}.{ModelName}Service;
import com.company.common.api.Result;

@Controller
public class {ModelName}Controller {
	@Autowired
	private {ModelName}Service {modelName}Service;

	@RequestMapping(value = "/admin/{module}/{modelName}/index", method = RequestMethod.GET)
	public String index(Model model, {ModelName} {modelName}, String createTimeStart, String createTimeEnd,
			String updateTimeStart, String updateTimeEnd) {
		model.addAttribute("search", {modelName});
		model.addAttribute("createTimeStart", createTimeStart);
		model.addAttribute("createTimeEnd", createTimeEnd);
		model.addAttribute("updateTimeStart", updateTimeStart);
		model.addAttribute("updateTimeEnd", updateTimeEnd);
		
		Wrapper<{ModelName}> searchWrapper = toSearchWrapper({modelName}, createTimeStart, createTimeEnd, updateTimeStart,
				updateTimeEnd);
		Page<{ModelName}> pageResult = {modelName}Service.selectPage(SelectPageUtil.page(), searchWrapper);
		
		model.addAttribute("pageModel", XSPageModel.build(pageResult.getRecords(), pageResult.getTotal()));
		
		return "{module}/{modelName}";
	}

	@RequestMapping(value = "/admin/{module}/{modelName}/create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		return "{module}/{modelName}Create";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/{module}/{modelName}/save", method = RequestMethod.POST)
	public Result<Void> save(@Validated({ModelName}.Save.class) {ModelName} {modelName}) {
		{modelName}Service.insert({modelName});
		return Result.success();
	}

	@RequestMapping(value = "/admin/{module}/{modelName}/update", method = RequestMethod.GET)
	public String update(Model model, HttpServletRequest request, {ModelName} {modelName}) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("{modelName}", {modelName}Service.selectById({modelName}.getId()));
		return "{module}/{modelName}Update";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/{module}/{modelName}/update", method = RequestMethod.POST)
	public Result<Void> update(@Validated({ModelName}.Update.class) {ModelName} {modelName}) {
		{modelName}Service.updateById({modelName});
		return Result.success();
	}

	@RequestMapping(value = "/admin/{module}/{modelName}/detail", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, {ModelName} {modelName}) {
		model.addAttribute("backUrl", request.getHeader("Referer"));
		model.addAttribute("{modelName}", {modelName}Service.selectById({modelName}.getId()));
		return "{module}/{modelName}Detail";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/{module}/{modelName}/get", method = RequestMethod.POST)
	public Result<{ModelName}> get({ModelName} {modelName}) {
		return Result.success({modelName}Service.selectById({modelName}.getId()));
	}

	@ResponseBody
	@RequestMapping(value = "/admin/{module}/{modelName}/remove", method = RequestMethod.POST)
	public Result<Void> remove({ModelName} {modelName}) {
		{modelName}Service.deleteById({modelName}.getId());
		return Result.success();
	}
    
    private Wrapper<{ModelName}> toSearchWrapper({ModelName} {modelName}, String createTimeStart,
			String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
    	Wrapper<{ModelName}> wrapper = new EntityWrapper<{ModelName}>();
{search_form}		if (StringUtils.isNotBlank(createTimeStart)) {
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