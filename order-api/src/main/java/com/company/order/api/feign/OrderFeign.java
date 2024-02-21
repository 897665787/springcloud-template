package com.company.order.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.feign.fallback.OrderFeignFallback;
import com.company.order.api.request.ChangeOrderStatusReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/order", fallbackFactory = OrderFeignFallback.class)
public interface OrderFeign {

	/**
	 * 注册订单
	 * 
	 * @param registerOrderReq
	 * @return
	 */
	@PostMapping("/registerOrder")
	Result<OrderResp> registerOrder(@RequestBody RegisterOrderReq registerOrderReq);

	/**
	 * 修改订单状态
	 * 
	 * @param changeOrderStatusReq
	 * @return
	 */
	@PostMapping("/changeStatus")
	Result<OrderResp> changeStatus(@RequestBody ChangeOrderStatusReq changeOrderStatusReq);

	/**
	 * 分页查询订单列表
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/page")
	Result<List<OrderResp>> page(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
			@RequestParam(value = "status", required = false) OrderEnum.StatusEnum status);

	/**
	 * 根据订单号查询订单详情
	 * 
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/queryByOrderCode")
	Result<OrderResp> queryByOrderCode(@RequestParam("orderCode") String orderCode);

	/**
	 * 根据订单号取消订单
	 * 
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/cancel")
	Result<OrderResp> cancel(@RequestParam("orderCode") String orderCode);
}
