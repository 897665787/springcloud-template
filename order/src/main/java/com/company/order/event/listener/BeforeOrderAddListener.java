package com.company.order.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.company.order.entity.Order;
import com.company.order.event.BeforeOrderAddEvent;

@Component
public class BeforeOrderAddListener implements ApplicationListener<BeforeOrderAddEvent> {

	@Override
	public void onApplicationEvent(BeforeOrderAddEvent event) {
		System.out.println("MyEventListener3.onApplicationEvent():" + event);
		long timestamp = event.getTimestamp();
		Order order = (Order) event.getSource();
		System.out.println("timestamp:" + timestamp + "order" + order);
		order.setOrderCode(String.valueOf(System.currentTimeMillis()));
	}
}
