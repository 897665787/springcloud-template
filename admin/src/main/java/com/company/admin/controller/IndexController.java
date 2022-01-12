package com.company.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.company.admin.entity.Order;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/index")
public class IndexController {

	// get
	@GetMapping(value = "/page")
	public String page(Model model) {
//		ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("username","jackson");
//        modelAndView.setViewName("beetl/test");
		System.out.println("page 11");
		Map<String, String> aa = Maps.newHashMap();
		aa.put("aaa", "bbbbb");
		model.addAttribute("item", aa);
		model.addAttribute("aaa", "111111");
		
		List<Order> orderList = Lists.newArrayList();
		orderList.add(new Order().setId(1L).setOrderCode("").setType(1));
		orderList.add(new Order().setId(2L).setOrderCode("2").setType(2));
		model.addAttribute("orderList", orderList);
		return "index";
//		return "index";
//		return "/index.html";
//		return "index.html";
	}
	
	// get
	@GetMapping(value = "/page2")
	public ModelAndView page2() {
		ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username","jackson");
        modelAndView.setViewName("index");
		System.out.println("page 2");
		return modelAndView;
	}
	
	// get
	@GetMapping(value = "/login")
	public String login() {
//		ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("username","jackson");
//        modelAndView.setViewName("beetl/test");
		System.out.println("page 11");
		return "login";
//		return "index";
//		return "/index.html";
//		return "index.html";
	}
}
