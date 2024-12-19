package com.company.web.controller;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.autoconfigure.ThreadPoolProperties;
import com.company.framework.cache.ICache;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.response.OrderResp;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;
import com.company.web.amqp.rabbitmq.Constants;
import com.company.web.amqp.strategy.StrategyConstants;
import com.company.web.amqp.strategy.dto.UserMQDto;
import com.company.web.service.TimeService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

	@Autowired
	private OrderFeign orderFeign;
	@Autowired
	private UserFeign userFeign;
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
	public Result<String> sendMessage(String message) {
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
		return Result.success("success");
	}

	@GetMapping(value = "/sendDelayMessage")
	public Result<String> sendDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.DEAD_LETTER.ROUTING_KEY, delaySeconds);
		return Result.success("success");
	}
	
	@GetMapping(value = "/sendXDelayMessage")
	public Result<String> sendXDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, Constants.EXCHANGE.XDELAYED,
				Constants.QUEUE.XDELAYED.ROUTING_KEY, delaySeconds);
		return Result.success("success");
	}

	@GetMapping(value = "/beans")
	public Result<Map<?,?>> beans() {
		ApplicationContext context = SpringContextUtil.getContext();
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		Map<String, Object> map = Maps.newHashMap();
		map.put("beanDefinitionNames", beanDefinitionNames);
		return Result.success(map);
	}
	
	@Autowired
	private ICache cache;
	
	@GetMapping(value = "/timestr")
	public Result<String> timestr() {
		String string1 = cache.get("aaaaaaa", () -> {
//			int a = 1/0;
			return ""+System.currentTimeMillis();
//			return null;
		});
		System.out.println(string1);
		
//		String string2 = RedisUtils.get("aaaaaaa", () -> {
////			int a = 1/0;
//			return ""+System.currentTimeMillis();
//		});
//		System.out.println(string2);
		return Result.success(timeService.getTime());
	}

	@Autowired
	private ThreadPoolProperties threadPoolProperties;
	@Value("${template.threadpool.maxPoolSize}")
	private Integer maxPoolSize;

    @GetMapping(value = "/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("threadPoolProperties", threadPoolProperties);
        map.put("maxPoolSize", maxPoolSize);
        return Result.success(map);
    }

	@RequireLogin
	@GetMapping(value = "/getOrderById")
	public Result<OrderResp> getOrderById(Long id) {
//		if (true)
//			throw new BusinessException(1, "aaaaaaaaaaa");
//		Result<OrderResp> byId = orderFeign.getById(id);
		System.out.println("currentUserId:" + HttpContextUtil.currentUserId());
//		return byId;
		return Result.success();
	}
	
	@GetMapping(value = "/getUserById")
	public Result<UserResp> getUserById(Long id) {
		Result<UserResp> byId = userFeign.getById(1L);
		System.out.println("byId:"+JsonUtil.toJsonString(byId));
		return byId;
	}
	
	@GetMapping(value = "/getInt")
	public Result<Integer> getInt(HttpServletRequest request) {
		String thname = Thread.currentThread().getName();
		System.out.println(thname+" "+"ApiController platform():" + HttpContextUtil.platform());
		System.out.println(thname+" "+"ApiController httpContextHeader():" + HttpContextUtil.httpContextHeader());
//		threadPoolTaskExecutor.submit(()->{
//			String thname2 = Thread.currentThread().getName();
//			System.err.println(thname2+" "+"ApiController submit platform():" + HttpContextUtil.platform());
////			System.out.println(thname2+" "+"ApiController submit httpContextHeader():" + HttpContextUtil.httpContextHeader());
//		});
		
		Future<Integer> submit = threadPoolTaskExecutor.submit(()->{
		String thname2 = Thread.currentThread().getName();
			System.err.println(thname2+" "+"ApiController submit request platform():" + request.getHeader(HttpContextUtil.HEADER_PLATFORM));
			System.err.println(thname2+" "+"ApiController submit platform():" + HttpContextUtil.platform());
	//		System.out.println(thname2+" "+"ApiController submit httpContextHeader():" + HttpContextUtil.httpContextHeader());
		return 1;
		});
		
//		List<Integer> list = Lists.newArrayList(1,2,3,4);
//		List<Integer> collect = list.parallelStream().map(a->{
//			return a+1;
//		}).collect(Collectors.toList());
		
//		threadPoolTaskExecutor.execute(()->{
//			String thname2 = Thread.currentThread().getName();
//			System.err.println(thname2+" "+"ApiController execute platform():" + HttpContextUtil.platform());
////			System.out.println(thname2+" "+"ApiController execute httpContextHeader():" + HttpContextUtil.httpContextHeader());
//		});
		return Result.success(1);
	}
	
	@GetMapping(value = "/getString")
	public Result<String> getString() {
		return Result.success("addddddd");
	}
	
	@GetMapping(value = "/time")
	public Result<Date> time() {
		return Result.success(new Date());
	}
	
	@GetMapping(value = "/onoff")
	public Result<OrderResp> onoff() {
		for (int i = 0; i < 3000; i++) {
//			Result<OrderResp> byId = orderFeign.getById((long)i);
//			log.info("onoff:{}",byId);
		}
		return Result.success(new OrderResp());
	}

	@GetMapping(value = "/send")
	public Result<String> send() {
		return Result.success("{}");
	}

	@Autowired
	SequenceGenerator sequenceGenerator;
	
	@GetMapping(value = "/retryGet")
	public Result<OrderResp> retryGet(Long id) {
		log.info("retryGet");
//		Result<OrderResp> byId = orderFeign.retryGet(id);
//		log.info("retryGet:{}", byId);
//		return byId;
		return Result.success();
	}

	@GetMapping(value = "/retryPost")
	public Result<OrderResp> retryPost(Long id) {
		log.info("retryPost");
//		Result<OrderResp> byId = orderFeign.retryPost(new OrderReq().setId(id));
//		log.info("retryPost:{}", byId);
//		return byId;
		return Result.success();
	}
	
	@GetMapping(value = "/threadpool")
	public Result<Integer> threadpool() {
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
		return Result.success(1);
	}
	
	@GetMapping(value = "/threadpooltask")
	public Result<Integer> threadpooltask() {
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
		return Result.success(1);
	}
	
	@GetMapping(value = "/log")
	public Result<Integer> log() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			try{
				int j = i/0;
				System.out.println(j);
			}catch(Exception e){
				log.error("log error", e);
			}
			log.info("log:{}", i);
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:"+(end-start));
		log.info("cost:{}", (end-start));
		return Result.success(1);
	}
}
