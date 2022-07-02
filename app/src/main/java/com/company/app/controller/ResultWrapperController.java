package com.company.app.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resultwrapper")
public class ResultWrapperController {
	
	@PostMapping(value = "/post-body-row")
	public Map<String, Object> postbodyrow(@RequestBody Map<String, Object> param) {
		return param;
	}
	
	@PostMapping(value = "/post-body-row2")
	public String postbodyrow2(@RequestBody Map<String, Object> param) {
		return "SUCCESS";
	}
}
