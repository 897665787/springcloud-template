package com.company.web.controller;

import com.company.common.api.Result;
import com.company.web.service.TimeService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cache")
@Slf4j
public class CacheController {

	@Autowired
	private TimeService timeService;

	@Cacheable(value = "users", key = "#id")
	@GetMapping(value = "/getById")
	public Result<String> getById(String id) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		return Result.success("success");
	}

	@GetMapping(value = "/time")
	public Result<String> time() {
		String time = timeService.getTime();
		return Result.success(time);
	}

	@GetMapping(value = "/cachetime")
	public Result<String> cachetime() {
		String time = timeService.getCacheTime();
		return Result.success(time);
	}
}
