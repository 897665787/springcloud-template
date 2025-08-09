package com.company.web.controller;

import com.company.common.api.Result;
import com.company.framework.context.HeaderContextUtil;
import com.company.framework.util.JsonUtil;
import com.company.order.api.feign.FeignTestFeign;
import com.company.order.api.response.OrderDetailResp;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/context")
@Slf4j
public class ContextController {

	@Autowired
	private AsyncTaskExecutor executor;
	@Autowired
	private FeignTestFeign feignTestFeign;
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(value = "/thread")
	public Result<Integer> thread() {
		log.info("HeaderContextUtil.currentUserId:{}", HeaderContextUtil.currentUserId());
		log.info("HeaderContextUtil.headerMap:{}", JsonUtil.toJsonString(HeaderContextUtil.headerMap()));

		for (int i = 0; i < 5; i++) {
			int n = i;
			new Thread(() -> {
				log.info("{} HeaderContextUtil.currentUserId:{}", n, HeaderContextUtil.currentUserId());
				log.info("{} HeaderContextUtil.headerMap:{}", n, JsonUtil.toJsonString(HeaderContextUtil.headerMap()));
			}).start();
		}
		log.info("log end");
		return Result.success(1);
	}

	@GetMapping(value = "/threadpooltask")
	public Result<Integer> threadpooltask() {
		log.info("HeaderContextUtil.currentUserId:{}", HeaderContextUtil.currentUserId());
		log.info("HeaderContextUtil.headerMap:{}", JsonUtil.toJsonString(HeaderContextUtil.headerMap()));

		for (int i = 0; i < 5; i++) {
			int n = i;
			executor.submit(() -> {
				log.info("{} HeaderContextUtil.currentUserId:{}", n, HeaderContextUtil.currentUserId());
				log.info("{} HeaderContextUtil.headerMap:{}", n, JsonUtil.toJsonString(HeaderContextUtil.headerMap()));
			});
		}
		log.info("log end");
		return Result.success(1);
	}

	@GetMapping(value = "/forkjoin")
	public Result<Integer> forkjoin() {
		log.info("HeaderContextUtil.currentUserId:{}", HeaderContextUtil.currentUserId());
		log.info("HeaderContextUtil.headerMap:{}", JsonUtil.toJsonString(HeaderContextUtil.headerMap()));

		List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
		log.info("list:{}", list);

		List<String> strList = list.parallelStream()
				.map(n -> {
					log.info("{} HeaderContextUtil.currentUserId:{}", n, HeaderContextUtil.currentUserId());
					log.info("{} HeaderContextUtil.headerMap:{}", n, JsonUtil.toJsonString(HeaderContextUtil.headerMap()));
			return n.toString();
		}).collect(Collectors.toList());

		log.info("strList:{}", strList);

		return Result.success(1);
	}

	@GetMapping(value = "/completablefuture")
	public Result<Integer> completablefuture() {
		log.info("HeaderContextUtil.currentUserId:{}", HeaderContextUtil.currentUserId());
		log.info("HeaderContextUtil.headerMap:{}", JsonUtil.toJsonString(HeaderContextUtil.headerMap()));

		List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
		log.info("list:{}", list);

		List<CompletableFuture<String>> list2 = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			final Integer n = list.get(i);
			CompletableFuture<String> item = CompletableFuture.supplyAsync(() -> {
				log.info("{} HeaderContextUtil.currentUserId:{}", n, HeaderContextUtil.currentUserId());
				log.info("{} HeaderContextUtil.headerMap:{}", n, JsonUtil.toJsonString(HeaderContextUtil.headerMap()));
				return n.toString();
			});
			list2.add(item);
		}
		CompletableFuture.allOf(list2.toArray(new CompletableFuture[0])).join();
		return Result.success(1);
	}

	@GetMapping(value = "/feign")
	public Result<Integer> feign() {
		log.info("HeaderContextUtil.currentUserId:{}", HeaderContextUtil.currentUserId());
		log.info("HeaderContextUtil.headerMap:{}", JsonUtil.toJsonString(HeaderContextUtil.headerMap()));

		for (int i = 0; i < 5; i++) {
			int n = i;
			executor.submit(() -> {
//				Result<OrderDetailResp> result = feignTestFeign.context();
//				log.info("{} feignTestFeign.context:{}", n, JsonUtil.toJsonString(result));

				Result result = restTemplate.getForObject("http://template-order/feignTest/context", Result.class);
				log.info("result:{}", JsonUtil.toJsonString(result));
				return result;
			});
		}
		log.info("log end");
		return Result.success(1);
	}
}
