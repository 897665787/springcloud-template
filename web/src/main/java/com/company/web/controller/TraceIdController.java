package com.company.web.controller;

import com.company.common.api.Result;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/traceid")
@Slf4j
public class TraceIdController {

	@Autowired
	private AsyncTaskExecutor executor;

	@GetMapping(value = "/thread")
	public Result<Integer> thread() {
		log.info("log begin");
		for (int i = 0; i < 5; i++) {
			int n = i;
			new Thread(() -> {
				log.info("log in thread:{}", n);
			}).start();
		}
		log.info("log end");
		return Result.success(1);
	}

	@GetMapping(value = "/threadpool")
	public Result<Integer> threadpool() {
		log.info("log begin");
		for (int i = 0; i < 5; i++) {
			int n = i;
			executor.execute(() -> {
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
			executor.submit(() -> {
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

		List<String> strList = list.parallelStream()
				.map(v -> {
			log.info("log in forkjoin:{}", v);
			return v.toString();
		}).collect(Collectors.toList());

		log.info("strList:{}", strList);

		return Result.success(1);
	}

	@GetMapping(value = "/completablefuture")
	public Result<Integer> completablefuture() {
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
		return Result.success(1);
	}
}
