package com.company.gateway.amqp.springevent.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class MessageEvent extends ApplicationEvent {

	private final Map<String, Object> headers = new HashMap<String, Object>();
	
	private String jsonStrMsg;
	
	private String exchange;
	
	public MessageEvent() {
		super("0");
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}
	
	public void setHeader(String key, Object value) {
		this.headers.put(key, value);
	}

	public String getJsonStrMsg() {
		return jsonStrMsg;
	}

	public void setJsonStrMsg(String jsonStrMsg) {
		this.jsonStrMsg = jsonStrMsg;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	
}
