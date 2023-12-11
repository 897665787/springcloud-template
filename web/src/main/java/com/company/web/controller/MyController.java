package com.company.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/my")
public class MyController {

	@GetMapping("/index")
	public String index(ModelAndView view) {
		view.addObject("nickname", "JQ棣");
		view.addObject("avator", "https://p.qqan.com/up/2022-10/16651900324263731.jpg");
		return "my";
	}
}
