package com.company.tool;

import com.lmax.disruptor.EventFactory;

public class HelloEventFactory implements EventFactory<MessageModel> {
	@Override
	public MessageModel newInstance() {
		return new MessageModel();
	}
}
