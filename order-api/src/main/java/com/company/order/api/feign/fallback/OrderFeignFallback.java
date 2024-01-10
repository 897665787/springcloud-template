package com.company.order.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
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
			public Result<OrderResp> getById(Long id) {
				log.error("getById error", e);
				return Result.onFallbackError();
			}

			public Result<OrderResp> save(OrderReq orderReq) {
				log.error("save error", e);
				return Result.onFallbackError();
			}

			@Override
			public Result<OrderResp> retryGet(Long id) {
				log.error("retryGet error", e);
				return Result.onFallbackError();
			}

			@Override
			public Result<OrderResp> retryPost(OrderReq orderReq) {
				log.error("retryPost error", e);
				return Result.onFallbackError();
			}
		};
	}
}
