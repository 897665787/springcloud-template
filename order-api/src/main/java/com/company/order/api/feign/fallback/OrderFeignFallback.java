package com.company.order.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderFeignFallback implements FallbackFactory<OrderFeign> {

	@Override
	public OrderFeign create(final Throwable e) {
		return new OrderFeign() {
			public OrderResp getById(Long id) {
				log.error("getById error", e);
				return new OrderResp().setOrderCode("Fallback");
			}

			public OrderResp save(OrderReq orderReq) {
				log.error("save error", e);
				return new OrderResp().setOrderCode("Fallback");
			}
		};
	}
}
