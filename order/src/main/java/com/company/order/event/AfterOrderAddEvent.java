package com.company.order.event;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class AfterOrderAddEvent extends ApplicationEvent {

	public AfterOrderAddEvent(Object source) {
		super(source);
	}

}
