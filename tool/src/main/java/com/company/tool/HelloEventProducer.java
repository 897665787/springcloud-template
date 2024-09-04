package com.company.tool;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;

public class HelloEventProducer {
	RingBuffer<MessageModel> ringBuffer;
	
	public HelloEventProducer(RingBuffer<MessageModel> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public synchronized void sayHelloMq(String message) {
		EventTranslator<MessageModel> et = (event, sequence) -> {
			event.setMessage(message);
		};
		ringBuffer.publishEvent(et);
		
		//
//		long next = ringBuffer.next();
//		MessageModel messageModel = ringBuffer.get(next);
//		messageModel.setMessage(message);
//		ringBuffer.publish(next);
	}

}
