package com.company.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.deploy.RefreshHandler;
import com.company.framework.redis.RedisHolder;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

	@Autowired
	private OrderFeign orderFeign;
	@Autowired
	private UserFeign userFeign;
	@Autowired
	private RefreshHandler refreshHandler;
	@Autowired(required = false)
	private ThreadPoolExecutor threadPoolExecutor;
	@Autowired(required = false)
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@GetMapping(value = "/info")
	public String info() {
		return "{}";
	}
	
	@GetMapping(value = "/getOrderById")
	public OrderResp getOrderById(Long id) {
//		if (true)
//			throw new BusinessException(1, "aaaaaaaaaaa");
		OrderResp byId = orderFeign.getById(id);
		System.out.println("currentUserId:" + HttpContextUtil.currentUserId());
		return byId;
	}
	
	@GetMapping(value = "/getUserById")
	public UserResp getUserById(Long id) {
		UserResp byId = userFeign.getById(1L);
		System.out.println(JsonUtil.toJsonString(byId));
		return byId;
	}
	
	@GetMapping(value = "/getInt")
	public Integer getInt() {
		return 1;
	}
	
	@GetMapping(value = "/getString")
	public String getString() {
		return "addddddd";
	}
	
	@GetMapping(value = "/time")
	public Date time() {
		return new Date();
	}
	
	@GetMapping(value = "/onoff")
	public OrderResp onoff() {
		for (int i = 0; i < 3000; i++) {
			OrderResp byId = orderFeign.getById((long)i);
			log.info("onoff:{}",byId);
		}
		return new OrderResp();
	}

	@GetMapping(value = "/send")
	public String send() {
		refreshHandler.notify2Refresh("test");
		return "{}";
	}

	@Autowired
	RedisHolder redisHolder;
	@Autowired
	SequenceGenerator sequenceGenerator;
	
	@GetMapping(value = "/retryGet")
	public OrderResp retryGet(Long id) {
		OrderResp aa = new OrderResp().setId(1L).setOrderCode(sequenceGenerator.nextId()+"").setType(1).setDate(new Date());
		redisHolder.set("aaaa", JsonUtil.toJsonString(aa));
		
//		ValueOperations opsForValue2 = redisTemplate.opsForValue();
		redisHolder.set("aa2", aa);
		
		redisHolder.set("aa4", "sadsd");
		
		OrderResp aaa = redisHolder.get("aa2", OrderResp.class);
		System.out.println(aaa);
		
		List<OrderResp> list = Lists.newArrayList();
		list.add(aa);
		redisHolder.set("aa3", list);
		
		List<OrderResp> list2 = redisHolder.getList("aa3",OrderResp.class);
		System.out.println(list2);
		
		log.info("retryGet");
		OrderResp byId = orderFeign.retryGet(id);
		log.info("retryGet:{}", byId);
		return byId;
	}

	@GetMapping(value = "/retryPost")
	public OrderResp retryPost(Long id) {
		log.info("retryPost");
		OrderResp byId = orderFeign.retryPost(new OrderReq().setId(id));
		log.info("retryPost:{}", byId);
		return byId;
	}
	
	@GetMapping(value = "/threadpool")
	public Integer threadpool() {
		System.out.println("threadPoolExecutor:"+threadPoolExecutor);
		for (int i = 0; i < 3; i++) {
			int n = i;
			threadPoolExecutor.submit(() -> {
				System.out.println("execute:"+n);
				try {
					Thread.sleep(new Random().nextInt(1000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			System.out.println("submit:"+n);
		}
		return 1;
	}
	
	@GetMapping(value = "/threadpooltask")
	public Integer threadpooltask() {
		System.out.println("threadPoolTaskExecutor:"+threadPoolTaskExecutor);
		for (int i = 0; i < 3; i++) {
			int n = i;
			threadPoolTaskExecutor.submit(() -> {
				System.out.println("execute:"+n);
				try {
					Thread.sleep(new Random().nextInt(1000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			System.out.println("submit:"+n);
		}
		
		threadPoolTaskExecutor.getActiveCount();
		return 1;
	}
	
	@GetMapping(value = "/log")
	public Integer log() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			try{
				
				int j = i/0;
			}catch(Exception e){
				log.error("log error", e);
			}
			log.info("log:{}", i);
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:"+(end-start));
		log.info("cost:{}", (end-start));
		return 1;
	}
}
