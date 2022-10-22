package com.company.admin.controller.interact;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import com.company.admin.service.interact.DemoService;
import com.company.admin.annotation.Pagination;
import com.company.admin.entity.interact.Demo;

/**
 * 实例代码，项目中请删除
 */
@Controller
@Deprecated
public class DemoController {
	@Autowired
	private DemoService demoService;
	
	@RequestMapping(value = "/admin/interact/demo", method = RequestMethod.GET)
	@Pagination
	public String index(Model model, Demo demo) {
		model.addAttribute("search", demo);
		model.addAttribute("pageModel", demoService.listAndCount(demo));
		return "interact/demo";
	}
	
	@RequestMapping(value = "/admin/interact/demo/detail", method = RequestMethod.GET)
	@Pagination
	public String detail(Model model, HttpServletRequest request, Demo demo) throws Exception {
		model.addAttribute("demo", demoService.get(demo));
		return "interact/demoDetail";
	}
}
