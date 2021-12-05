package com.company.web.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.NoResultWrapper;
import com.company.web.req.Param;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/reqlog")
public class ReqLogController {

	// get

	@GetMapping(value = "/get-params-none")
	public String getparamsnone() {
		// GET /reqlog/get-params-none {} 2
		return "1";
	}

	@GetMapping(value = "/get-params")
	public String getparams(String p) {
		// GET /reqlog/get-params {"p":"12"} 11
		return p;
	}

	@GetMapping(value = "/get-body-form-data")
	public Map<String, Object> getbodyformdata(String p1, String p2, String p3) {
		// GET /reqlog/get-body-form-data {} 224
		// 获取不到参数
		Map<String, Object> map = Maps.newHashMap();
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		return map;
	}

	@GetMapping(value = "/get-body-form-data2")
	public Param getbodyformdata2(Param map) {
		// GET /reqlog/get-body-form-data2 {} 8
		// 获取不到参数
		return map;
	}

	@GetMapping(value = "/get-body-x-www-form-urlencoded")
	public Map<String, Object> getbodyurlencoded(String p1, String p2, String p3) {
		// GET /reqlog/get-body-x-www-form-urlencoded {} 3
		// 获取不到参数
		Map<String, Object> map = Maps.newHashMap();
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		return map;
	}

	@GetMapping(value = "/get-body-x-www-form-urlencoded2")
	public Param getbodyurlencoded2(Param map) {
		// GET /reqlog/get-body-x-www-form-urlencoded2 {} 6
		// 获取不到参数
		return map;
	}

	@GetMapping(value = "/get-body-row")
	public Map<String, Object> getbodyrow(@RequestBody Map<String, Object> param) {
		// GET /reqlog/get-body-row {"asd":1222,"sadsad":" asd asd "} 2490
		return param;
	}

	// post

	@PostMapping(value = "/post-params-none")
	public String postparamsnone() {
		return "1";
	}

	@PostMapping(value = "/post-params")
	public String postparams(String p) {
		// POST /reqlog/post-params {"p":"1"} 14
		return p;
	}

	@PostMapping(value = "/post-body-form-data")
	public Map<String, Object> postbodyformdata(String p1, String p2, String p3) {
		// POST /reqlog/post-body-form-data {"p1":"1","p2":"2"} 6
		Map<String, Object> map = Maps.newHashMap();
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		return map;
	}

	@PostMapping(value = "/post-body-form-data2")
	public Param postbodyformdata2(Param map) {
		// POST /reqlog/post-body-form-data2 {"p1":"1","p2":"2"} 11
		return map;
	}

	@PostMapping(value = "/post-body-x-www-form-urlencoded")
	public Map<String, Object> postbodyurlencoded(String p1, String p2, String p3) {
		// POST /reqlog/post-body-x-www-form-urlencoded {"p1":"1"} 9
		Map<String, Object> map = Maps.newHashMap();
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		return map;
	}

	@PostMapping(value = "/post-body-row")
	public Map<String, Object> postbodyrow(@RequestBody Map<String, Object> param) {
		// POST /reqlog/post-body-row {"asdasd":1,"sadsaddd":" asdas dasd"} 5
		return param;
	}
	
	@PostMapping(value = "/post-body-row-noresult")
	@NoResultWrapper
	public Map<String, Object> postbodyrownoresult(@RequestBody Map<String, Object> param) {
		// POST /reqlog/post-body-row {"asdasd":1,"sadsaddd":" asdas dasd"} 5
		return param;
	}
	
	@PostMapping(value = "/post-body-row-noresult2")
	@NoResultWrapper
	public String postbodyrownoresult2(@RequestBody Map<String, Object> param) {
		// POST /reqlog/post-body-row {"asdasd":1,"sadsaddd":" asdas dasd"} 5
		return "SUCCESS";
	}
}
