package com.company.tool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisTest {
	public static void main(String[] args) {
		ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<Integer>(100);
		LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<Integer>();
		 //define the thread pool for consumer message hander， Disruptor touch the consumer event to process by java.util.concurrent.ExecutorSerivce
//        ExecutorService executor = Executors.newFixedThreadPool(2);
        //define Event Factory
        EventFactory<MessageModel> factory = new HelloEventFactory();
        //ringbuffer byte size
        int bufferSize = 1024 * 256;
        //单线程模式，获取额外的性能
//        public Disruptor(
//                final EventFactory<T> eventFactory,
//                final int ringBufferSize,
//                final ThreadFactory threadFactory,
//                final ProducerType producerType,
//                final WaitStrategy waitStrategy)
		ThreadFactory threadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);
			}
		};
		
		Disruptor<MessageModel> disruptor = new Disruptor<MessageModel>(factory, bufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());
//        Disruptor<MessageModel> disruptor = new Disruptor<MessageModel>(factory, bufferSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());
//        Disruptor<MessageModel> disruptor = new Disruptor<>(factory, bufferSize, executor, ProducerType.SINGLE, new BlockingWaitStrategy());
        //set consumer event
//        disruptor.handleEventsWith(new HelloEventHandler());
//        disruptor.getSequenceValueFor(null)
        //start disruptor thread
        disruptor.start();
        System.out.println("start");
        //gain ringbuffer ring，to product event
        RingBuffer<MessageModel> ringBuffer = disruptor.getRingBuffer();

//        MessageModel aaa = ringBuffer.claimAndGetPreallocated(bufferSize);
//		Thread p = new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				HelloEventProducer producer = new HelloEventProducer(ringBuffer);
				producer.sayHelloMq("sdddddd-" + i);
				System.out.println("sdddddd-" + i);
			}
//		});
//		p.start();
//
//
			System.out.println(11111);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		Thread c = new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				long next = ringBuffer.getCursor();
//				long next = ringBuffer.next();
				System.out.println("next:"+next);
				MessageModel messageModel = ringBuffer.get(next);
				System.out.println(messageModel);
			}
		});
		c.start();
		
		System.out.println(11111);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//        disruptor.shutdown();
//        System.out.println("shutdown");
	}
}
