package com.company.order.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderFeignFallback implements FallbackFactory<OrderFeign> {

	@Override
	public OrderFeign create(final Throwable e) {
		return new OrderFeign() {
			public OrderResp getById(Long id) {
				log.error("aaaaaaaaaaaaaaaaaaaaa error", e);
				return new OrderResp().setOrderCode("Fallback");
			}

			public OrderResp save(OrderReq orderReq) {
				log.error("save error", e);
				return new OrderResp().setOrderCode("Fallback");
			}

			@Override
			public OrderResp retryGet(Long id) {
				log.error("retryGet error", e);
				return new OrderResp().setOrderCode("Fallback");
			}

			@Override
			public OrderResp retryPost(OrderReq orderReq) {
				log.error("retryPost error", e);
				return new OrderResp().setOrderCode("Fallback");
			}
		};
	}
}
