package com.company.tool;

import com.company.common.util.JsonUtil;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.Sequence;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloEventHandler implements EventHandler<MessageModel> {
	@Override
	public void onEvent(MessageModel event, long sequence, boolean endOfBatch) {
		try {

			Thread.sleep(1000);
			log.info("consume message start:"+sequence+":"+endOfBatch);
			if (event != null) {
				log.info("the message is：{}", event);
			}
		} catch (Exception e) {
			log.info("consume message fail");
		}
		log.info("consume message ending");
	}

	@Override
	public void setSequenceCallback(Sequence sequenceCallback) {
		System.out.println("HelloEventHandler.setSequenceCallback():"+JsonUtil.toJsonString(sequenceCallback));
		EventHandler.super.setSequenceCallback(sequenceCallback);
	}
	
}
