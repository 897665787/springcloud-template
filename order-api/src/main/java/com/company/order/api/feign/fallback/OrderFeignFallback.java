package com.company.order.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.order.api.enums.OrderEnum.StatusEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.request.OrderFinishReq;
import com.company.order.api.request.OrderPaySuccessReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;
import com.company.order.api.response.OrderResp;

import feign.hystrix.FallbackFactory;

@Component
public class OrderFeignFallback implements FallbackFactory<OrderFeign> {

	@Override
	public OrderFeign create(final Throwable e) {
		return new OrderFeign() {
			@Override
			public Result<OrderResp> registerOrder(RegisterOrderReq registerOrderReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<OrderDetailResp> cancelByUser(OrderCancelReq orderCancelReq) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<Void> cancelByTimeout(OrderCancelReq orderCancelReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> paySuccess(OrderPaySuccessReq orderPaySuccessReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> finish(OrderFinishReq orderFinishReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<OrderResp>> page(Integer current, Integer size, StatusEnum status) {
				return Result.onFallbackError();
			}

			@Override
			public Result<OrderDetailResp> queryByOrderCode(String orderCode) {
				return Result.onFallbackError();
			}

		};
	}
}
