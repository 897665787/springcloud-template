package com.company.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;
import com.company.user.entity.User;

@Component
public class UserService {

	@Autowired
	private OrderFeign orderFeign;

	public User getById(Long id) {
//		OrderResp orderResp = orderFeign.getById(1L);
//		System.out.println("orderResp:" + orderResp);

		OrderReq orderReq = new OrderReq().setId(3L).setOrderCode(String.valueOf(System.currentTimeMillis()));
		OrderResp save = orderFeign.save(orderReq);
		System.out.println("save:" + save);
		return new User().setId(id).setUsername(String.valueOf(System.currentTimeMillis()));
	}
}
