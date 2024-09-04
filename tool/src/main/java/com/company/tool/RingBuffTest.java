package com.company.tool;

import cn.hutool.core.codec.Base64;

public class RingBuffTest {
	public static void main(String[] args) {
		for (int i = 0; i < 100000; i++) {
			String encode = Base64.encode(""+i);
			System.out.println(i+"  "+encode);
		}
//		Base64Utils.
//		new RingBuffer
	}
}
