package com.company.web.controller;

import com.company.common.api.Result;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.cache.ICache;
import com.company.framework.constant.HeaderConstants;
import com.company.framework.context.HeaderContextUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.sequence.SequenceGenerator;
import com.company.framework.threadpool.ThreadPoolProperties;
import com.company.framework.util.JsonUtil;
import com.company.framework.util.PropertyUtils;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.response.Order4Resp;
import com.company.order.api.response.OrderDetailResp;
import com.company.order.api.response.OrderResp;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;
import com.company.web.service.TimeService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

	@Autowired
	private OrderFeign orderFeign;
	@Autowired
	private UserFeign userFeign;
	@Autowired
	private AsyncTaskExecutor executor;
	@Autowired
	private TimeService timeService;

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
	public Result<Order4Resp> getOrderById(String orderCode) {
//		if (true)
//			ExceptionUtil.throwException(1, "aaaaaaaaaaa");
//        Result<OrderDetailResp> detail = orderFeign.detail(orderCode);
        Order4Resp order4RespResult = orderFeign.selectByOrderCode(orderCode).dataOrThrow();
        return Result.success(order4RespResult);
	}

	@GetMapping(value = "/getUserById")
	public Result<com.company.web.resp.UserResp> getUserById(Long id) {
//		Result<UserResp> byId = userFeign.getById(1L);

		UserResp userResp = new UserResp();
		userResp.setId(1L);
		userResp.setStatus(1);
		Result<UserResp> byId = Result.success(userResp);
		System.out.println("byId:"+JsonUtil.toJsonString(byId));
		com.company.web.resp.UserResp resp = PropertyUtils.copyProperties(byId.dataOrThrow(), com.company.web.resp.UserResp.class);
		return Result.success(resp);
	}

	@GetMapping(value = "/getInt")
	public Result<Integer> getInt(HttpServletRequest request) {
		String thname = Thread.currentThread().getName();
		System.out.println(thname+" "+"ApiController platform():" + HeaderContextUtil.platform());
		System.out.println(thname+" "+"ApiController httpContextHeader():" + HeaderContextUtil.httpContextHeader());
//		threadPoolTaskExecutor.submit(()->{
//			String thname2 = Thread.currentThread().getName();
//			System.err.println(thname2+" "+"ApiController submit platform():" + HeaderContextUtil.platform());
////			System.out.println(thname2+" "+"ApiController submit httpContextHeader():" + HeaderContextUtil.httpContextHeader());
//		});

		Future<Integer> submit = executor.submit(()->{
		String thname2 = Thread.currentThread().getName();
			System.err.println(thname2+" "+"ApiController submit request platform():" + request.getHeader(HeaderConstants.HEADER_PLATFORM));
			System.err.println(thname2+" "+"ApiController submit platform():" + HeaderContextUtil.platform());
	//		System.out.println(thname2+" "+"ApiController submit httpContextHeader():" + HeaderContextUtil.httpContextHeader());
		return 1;
		});

//		List<Integer> list = Lists.newArrayList(1,2,3,4);
//		List<Integer> collect = list.parallelStream().map(a->{
//			return a+1;
//		}).collect(Collectors.toList());

//		threadPoolTaskExecutor.execute(()->{
//			String thname2 = Thread.currentThread().getName();
//			System.err.println(thname2+" "+"ApiController execute platform():" + HeaderContextUtil.platform());
////			System.out.println(thname2+" "+"ApiController execute httpContextHeader():" + HeaderContextUtil.httpContextHeader());
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
		System.out.println("executor:"+executor);
		for (int i = 0; i < 3; i++) {
			int n = i;
			executor.submit(() -> {
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
		System.out.println("executor:"+executor);
		for (int i = 0; i < 3; i++) {
			int n = i;
			executor.execute(() -> {
				System.out.println("execute:"+n);
				try {
					Thread.sleep(new Random().nextInt(1000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			System.out.println("submit:"+n);
		}

//		executor.getActiveCount();
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
