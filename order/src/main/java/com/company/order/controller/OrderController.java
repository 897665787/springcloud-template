package com.company.order.controller;

import java.util.List;

import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.util.PropertyUtils;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;
import com.company.order.elasticsearch.Product;
import com.company.order.elasticsearch.ProductESDAO;
import com.company.order.entity.Order;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController implements OrderFeign {

	@Autowired
	private ProductESDAO productESDAO;
	
	@Value("${server.port}")
	private String port;

	@GetMapping(value = "/info")
	public String info() {
		return "{}";
	}

	@GetMapping(value = "/list")
	public List<Order> list() {
		List<Order> list = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(new Order().setId(Long.valueOf(i)).setOrderCode(String.valueOf(System.currentTimeMillis())));
		}
		return list;
	}

	@Override
	public OrderResp getById(Long id) {
		log.info("provider-6001");
		
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
		
		return new OrderResp().setId(id).setOrderCode(String.valueOf(System.currentTimeMillis()));
	}
	
	@Override
	public OrderResp save(@RequestBody OrderReq orderReq) {
		Product aa = PropertyUtils.copyProperties(orderReq, Product.class);
		aa.setOrderCode(String.valueOf(System.currentTimeMillis()));
		productESDAO.save(aa);
		return PropertyUtils.copyProperties(orderReq, OrderResp.class);
	}
}
