package com.company.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.company.common.annotation.PublicUrl;

@PublicUrl
@Controller
// @RequestMapping("/index")
public class IndexController {

	@GetMapping("/index")
	public String index() {
//		int a= 1/0;
		return "index";
	}
}
