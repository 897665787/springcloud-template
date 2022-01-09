package com.company.admin.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@GetMapping(value = "/index")
	public String page(Model model) {
		System.out.println("page 11");
		Map<String, String> aa = Maps.newHashMap();
		aa.put("aaa", "bbbbb");
		model.addAttribute("item", aa);
		return "user/index";
	}
}
