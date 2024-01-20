package com.company.web.controller;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/traceid")
@Slf4j
public class TraceIdController {

	@Autowired(required = false)
	private ThreadPoolExecutor threadPoolExecutor;

	@Autowired(required = false)
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@GetMapping(value = "/threadpool")
	public Result<Integer> threadpool() {
		log.info("log begin");
		for (int i = 0; i < 5; i++) {
			int n = i;
			threadPoolExecutor.submit(() -> {
				log.info("log in threadpool:{}", n);
			});
		}
		log.info("log end");
		return Result.success(1);
	}

	@GetMapping(value = "/threadpooltask")
	public Result<Integer> threadpooltask() {
		log.info("log begin");
		for (int i = 0; i < 5; i++) {
			int n = i;
			threadPoolTaskExecutor.submit(() -> {
				log.info("log in threadpool:{}", n);
			});
		}
		log.info("log end");
		return Result.success(1);
	}

	@GetMapping(value = "/forkjoin")
	public Result<Integer> forkjoin() {
		List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
		log.info("list:{}", list);

		List<String> strList = list.parallelStream().map(v -> {
			log.info("log in forkjoin:{}", v);
			return v.toString();
		}).collect(Collectors.toList());

		log.info("strList:{}", strList);

		return Result.success(1);
	}
}
