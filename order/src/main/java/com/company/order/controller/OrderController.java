package com.company.order.controller;

import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.common.util.MdcUtil;
import com.company.common.util.PropertyUtils;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.redis.RedisUtils;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;
import com.company.order.entity.Order;
import com.company.order.event.AfterOrderAddEvent;
import com.company.order.event.BeforeOrderAddEvent;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController implements OrderFeign {
	
	@Value("${server.port}")
	private Integer port;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;

	@GetMapping(value = "/info")
	public String info() {
		return "{}";
	}
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private UserFeign userFeign;
	
	@GetMapping(value = "/list")
	public List<Order> list() {
		Order payload = new Order().setId(Long.valueOf(1));
		System.out.println("payload:"+payload);
		BeforeOrderAddEvent beforeOrderAddEvent = new BeforeOrderAddEvent(payload);
		applicationContext.publishEvent(beforeOrderAddEvent);
		System.out.println("payload save:"+payload);
		AfterOrderAddEvent afterOrderAddEvent = new AfterOrderAddEvent(payload);
		applicationContext.publishEvent(afterOrderAddEvent);
		System.out.println("payload:"+payload);
		
		List<Order> list = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(new Order().setId(Long.valueOf(i)).setOrderCode(String.valueOf(System.currentTimeMillis())));
		}
		return list;
	}

	@Override
	public Result<OrderResp> getById(Long id) {
		log.info("provider-6001:{}-{}", id, System.currentTimeMillis());
		System.out.println("OrderController thread:"+Thread.currentThread());
		HttpServletRequest request = HttpContextUtil.request();
		System.out.println("request:" + request);
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
//			System.out.println("headerName:" + headerName);
			String headerValue = request.getHeader(headerName);
			System.out.println("headerName:" + headerName + " headerValue:" + headerValue);
		}
		Result<UserResp> byId = userFeign.getById(1L);
		UserResp userResp = byId.getData();
		System.out.println("userResp:" + userResp);
		System.out.println("currentUserId:" + HttpContextUtil.currentUserId());
		
//		if(true){
//			throw new BusinessException("asdsad");
//		}
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		/*
//		FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
//                .add(QueryBuilders.matchPhraseQuery("searchQuery", "20"),
//                        ScoreFunctionBuilders.weightFactorFunction(100))
//                .scoreMode("sum")
//                .setMinScore(10);
		RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("id").from("3").to("6");
//		RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("orderCode").from("1595168537070").to("1595168534254");
//		MoreLikeThisQueryBuilder queryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{"70"});
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 3, sort);
         //es搜索默认第一页页码是0
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(queryBuilder)
                .build();
		Page<Product> page = productESDAO.search(searchQuery);
		System.out.println("page:" + page);
		System.out.println("page.getContent():" + page.getContent());
		*/
		return Result.success(new OrderResp().setId(id).setOrderCode(String.valueOf(sequenceGenerator.nextId())).setPort(port));
	}
	
	@Override
	public Result<OrderResp> save(@RequestBody OrderReq orderReq) {
		return Result.success(PropertyUtils.copyProperties(orderReq, OrderResp.class));
	}
	
	@Override
	public Result<OrderResp> retryGet(Long id) {
		log.info("retryGet:"+MdcUtil.get());
		try {
			int aa = new Random().nextInt(3) <2 ? 500:1000;
			System.out.println("aa:"+aa);
			Thread.sleep(aa);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long increment = RedisUtils.increment("jiang", 1);
		System.out.println("increment:"+increment);
//		UserResp userResp = userFeign.retryGet(1L);
//		log.info("retryGet:{}", userResp);
//		return new OrderResp().setOrderCode(userResp.getName());
		return Result.success(new OrderResp().setOrderCode("" + increment));
	}

	@Override
	public Result<OrderResp> retryPost(@RequestBody OrderReq orderReq) {
		log.info("retryPost");
		try {
			int aa = new Random().nextInt(3) <2 ? 500:1000;
			System.out.println("aa:"+aa);
			Thread.sleep(aa);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		UserResp userResp = userFeign.retryPost(new UserReq());
//		log.info("retryPost:{}", userResp);
		return Result.success(new OrderResp().setOrderCode(System.currentTimeMillis()+""));
	}
}
