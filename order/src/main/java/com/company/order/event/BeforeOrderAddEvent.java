package com.company.order.event;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class BeforeOrderAddEvent extends ApplicationEvent {

	public BeforeOrderAddEvent(Object source) {
		super(source);
	}

}
