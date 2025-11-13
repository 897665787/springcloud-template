package com.company.tool.retry.strategy;

import org.springframework.stereotype.Component;

/**
 * 微信等待策略（参考微信支付回调处理机制https://pay.weixin.qq.com/doc/v3/merchant/4012791861）
 * 总共15个预定义间隔：15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h
 * 
 * @author JQ棣
 */
@Component(WaitStrategyBeanFactory.WECHAT_WAIT_STRATEGY)
public class WechatWaitStrategy implements WaitStrategy {
	// 微信推荐的重试间隔策略（秒）
	// 总共15个预定义间隔: 15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h
	private static final int[] INTERVALS = {
		15,     // 第1次重试: 15秒
		15,     // 第2次重试: 15秒
		30,     // 第3次重试: 30秒
		180,    // 第4次重试: 3分钟
		600,    // 第5次重试: 10分钟
		1200,   // 第6次重试: 20分钟
		1800,   // 第7次重试: 30分钟
		1800,   // 第8次重试: 30分钟
		1800,   // 第9次重试: 30分钟
		3600,   // 第10次重试: 60分钟
		10800,  // 第11次重试: 3小时
		10800,  // 第12次重试: 3小时
		10800,  // 第13次重试: 3小时
		21600,  // 第14次重试: 6小时
		21600   // 第15次重试: 6小时
	};

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		// failure从0开始计数，对应数组索引
		if (failure < INTERVALS.length) {
			return INTERVALS[failure];
		}
		// 超出预定义范围时，使用最大间隔
		return INTERVALS[INTERVALS.length - 1];
	}

	public static void main(String[] args) {
		WechatWaitStrategy strategy = new WechatWaitStrategy();
		for (int i = 0; i < 20; i++) {
			System.out.println("Failure: " + i + ", Wait Seconds: " + strategy.nextSeconds(0, i));
		}
	}

}