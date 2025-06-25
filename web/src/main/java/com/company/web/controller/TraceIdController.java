package com.company.web.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/traceid")
@Slf4j
public class TraceIdController {

	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@GetMapping(value = "/thread")
	public Integer thread() {
		log.info("log begin");
		for (int i = 0; i < 5; i++) {
			int n = i;
			new Thread(() -> {
				log.info("log in thread:{}", n);
			}).start();
		}
		log.info("log end");
		return 1;
	}

	@GetMapping(value = "/threadpool")
	public Integer threadpool() {
		log.info("log begin");
		for (int i = 0; i < 5; i++) {
			int n = i;
			threadPoolExecutor.submit(() -> {
				log.info("log in threadpool:{}", n);
			});
		}
		log.info("log end");
		return 1;
	}

	@GetMapping(value = "/threadpooltask")
	public Integer threadpooltask() {
		log.info("log begin");
		for (int i = 0; i < 5; i++) {
			int n = i;
			threadPoolTaskExecutor.submit(() -> {
				log.info("log in threadpool:{}", n);
			});
		}
		log.info("log end");
		return 1;
	}

	@GetMapping(value = "/forkjoin")
	public Integer forkjoin() {
		List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
		log.info("list:{}", list);

		List<String> strList = list.parallelStream()
				.map(v -> {
			log.info("log in forkjoin:{}", v);
			return v.toString();
		}).collect(Collectors.toList());

		log.info("strList:{}", strList);

		return 1;
	}

	@GetMapping(value = "/completablefuture")
	public Integer completablefuture() {
		List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
		log.info("list:{}", list);

		List<CompletableFuture<String>> list2 = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			final Integer v = list.get(i);
			CompletableFuture<String> item = CompletableFuture.supplyAsync(() -> {
				log.info("log in completablefuture:{}", v);
				return v.toString();
			});
			list2.add(item);
		}
		CompletableFuture.allOf(list2.toArray(new CompletableFuture[0])).join();
		return 1;
	}
}
