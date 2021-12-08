package com.company.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.PublicUrl;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.deploy.RefreshHandler;
import com.company.framework.redis.RedisUtils;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;
import com.company.web.rabbitmq.Constants;
import com.company.web.rabbitmq.consumer.strategy.StrategyConstants;
import com.company.web.rabbitmq.dto.UserMQDto;
import com.company.web.service.TimeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@PublicUrl
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
	@Autowired
	private TimeService timeService;
	@Autowired
	private MessageSender messageSender;
//	private RabbitMessageSender messageSender;
	
	@GetMapping(value = "/sendMessage")
	public String sendMessage(String message) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("open2", message);

		messageSender.sendNormalMessage(StrategyConstants.MAP_STRATEGY,params, Constants.EXCHANGE.DIRECT, Constants.QUEUE.COMMON.ROUTING_KEY);
		
		messageSender.sendFanoutMessage(params, FanoutConstants.ORDER_CREATE.EXCHANGE);
		
		UserMQDto param = new UserMQDto();
		param.setP1("p1");
		param.setP2("p2");
		param.setP3("p3");
		messageSender.sendNormalMessage(StrategyConstants.USER_STRATEGY, param, Constants.EXCHANGE.DIRECT, Constants.QUEUE.COMMON.ROUTING_KEY);
		return "success";
	}

	@GetMapping(value = "/sendDelayMessage")
	public String sendDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.DEAD_LETTER.ROUTING_KEY, delaySeconds);
		return "success";
	}
	
	@GetMapping(value = "/sendXDelayMessage")
	public String sendXDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, Constants.EXCHANGE.XDELAYED,
				Constants.QUEUE.XDELAYED.ROUTING_KEY, delaySeconds);
		return "success";
	}

	@GetMapping(value = "/beans")
	public Map<?,?> beans() {
		ApplicationContext context = SpringContextUtil.getContext();
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		Map<String, Object> map = Maps.newHashMap();
		map.put("beanDefinitionNames", beanDefinitionNames);
		return map;
	}
	
	@GetMapping(value = "/timestr")
	public String timestr() {
		return timeService.getTime();
	}
	
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
	
	@PublicUrl
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
	SequenceGenerator sequenceGenerator;
	
	@GetMapping(value = "/retryGet")
	public OrderResp retryGet(Long id) {
		OrderResp aa = new OrderResp().setId(1L).setOrderCode(sequenceGenerator.nextId()+"").setType(1).setDate(new Date());
		RedisUtils.set("aaaa", JsonUtil.toJsonString(aa));
		
//		ValueOperations opsForValue2 = redisTemplate.opsForValue();
		RedisUtils.set("aa2", aa);
		
		RedisUtils.set("aa4", "sadsd");
		
		OrderResp aaa = RedisUtils.get("aa2", OrderResp.class);
		System.out.println(aaa);
		
		List<OrderResp> list = Lists.newArrayList();
		list.add(aa);
		RedisUtils.set("aa3", list);
		
		List<OrderResp> list2 = RedisUtils.getList("aa3",OrderResp.class);
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
