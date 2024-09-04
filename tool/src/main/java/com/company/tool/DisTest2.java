package com.company.tool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.company.common.util.JsonUtil;

public class DisTest2 {
	public static void main(String[] args) {
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
//		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();

		ExecutorService es1 = Executors.newFixedThreadPool(100);
		ExecutorService es2 = Executors.newFixedThreadPool(3);

		for (int i = 0; i < 1000; i++) {
			int n = i;
			es1.submit(() -> {
				try {
					queue.put(Integer.valueOf(n));
					System.out.println("es1:" + n);
					System.out.println("es1:" + JsonUtil.toJsonString(queue));
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		
		for (int i = 0; i < 1000; i++) {
			es2.submit(() -> {
				try {
					Integer n = queue.take();
					System.out.println("es2:" + n);
					System.out.println("es2:" + JsonUtil.toJsonString(queue));
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		
		es1.shutdown();
		es2.shutdown();
	}
}
