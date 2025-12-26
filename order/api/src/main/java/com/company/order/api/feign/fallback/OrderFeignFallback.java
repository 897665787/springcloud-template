package com.company.order.api.feign.fallback;

import java.util.ArrayList;
import java.util.List;

import com.company.common.api.Result;
import org.springframework.stereotype.Component;


import com.company.order.api.enums.OrderEnum.StatusEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.request.OrderFinishReq;
import com.company.order.api.request.OrderPaySuccessReq;
import com.company.order.api.request.OrderReceiveReq;
import com.company.order.api.request.OrderRefundApplyReq;
import com.company.order.api.request.OrderRefundFailReq;
import com.company.order.api.request.OrderRefundFinishReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.Order4Resp;
import com.company.order.api.response.OrderDetailResp;
import com.company.order.api.response.OrderRefundApplyResp;
import com.company.order.api.response.OrderResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class OrderFeignFallback implements FallbackFactory<OrderFeign> {

	@Override
	public OrderFeign create(final Throwable e) {
		return new OrderFeign() {
			@Override
			public Void registerOrder(RegisterOrderReq registerOrderReq) {
				return Result.onFallbackError();
			}

			@Override
			public OrderDetailResp cancelByUser(OrderCancelReq orderCancelReq) {
				return Result.onFallbackError();
			}
			
			@Override
			public Boolean cancelByTimeout(OrderCancelReq orderCancelReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean paySuccess(OrderPaySuccessReq orderPaySuccessReq) {
				return Result.onFallbackError();
			}
			
			@Override
			public Boolean receive(OrderReceiveReq orderReceiveReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean finish(OrderFinishReq orderFinishReq) {
				return Result.onFallbackError();
			}

			@Override
			public OrderRefundApplyResp refundApply(OrderRefundApplyReq orderRefundApplyReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean refundFail(OrderRefundFailReq orderRefundFailReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean refundFinish(OrderRefundFinishReq orderRefundFinishReq) {
				return Result.onFallbackError();
			}

			@Override
			public Void deleteOrder(String orderCode) {
				return Result.onFallbackError();
			}

            @Override
            public List<OrderResp> page(Integer current, Integer size, StatusEnum status) {
                return new ArrayList<>();// 降级返回空列表
            }

			@Override
			public OrderDetailResp detail(String orderCode) {
				return Result.onFallbackError();
			}

			@Override
			public List<String> select4OverSendSuccess(Integer limit) {
                return new ArrayList<>();// 降级返回空列表
			}
			
			@Override
			public List<String> select4OverWaitReview(Integer limit) {
                return new ArrayList<>();// 降级返回空列表
			}

			@Override
			public Order4Resp selectByOrderCode(String orderCode) {
				return Result.onFallbackError();
			}

		};
	}
}
