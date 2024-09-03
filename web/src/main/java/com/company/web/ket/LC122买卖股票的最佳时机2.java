package com.company.web.ket;

import java.util.Arrays;

public class LC122买卖股票的最佳时机2 {
	public static void main(String[] args) {
		int[] coins = new int[] { 7, 1, 5, 3, 6, 4 };
//		int[] coins = new int[] { 1, 2, 3, 4, 5 };
//		int[] coins = new int[] { 1, 2 };

		int coinChange = new LC122买卖股票的最佳时机2().maxProfit(coins);
		System.out.println(coinChange);
	}

	public int maxProfit(int[] prices) {
		int sum = 0;
		int s = 0;
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] < prices[i - 1]) {
				sum += maxProfit2(prices, s, i - 1);
				System.out.println("sum：" + sum + "  s:" + s + "  i-1:" + (i - 1) + "  i:" + (i));
				s = i;
			}
		}
		if (s < prices.length - 1) {
			sum += maxProfit2(prices, s, prices.length - 1);
		}
		return sum;
	}

	public int maxProfit2(int[] prices, int s, int e) {
		if (s == e) {
			return 0;
		}
		int length = e - s + 1;
		int[] max = new int[length];
		max[0] = 0;
		int left_min = prices[s];
		for (int i = s + 1; i <= e; i++) {
			max[i - s] = Integer.max(prices[i] - left_min, max[i - s - 1]);
			if (left_min > prices[i]) {
				left_min = prices[i];
			}
		}
		return max[length - 1];
	}
}