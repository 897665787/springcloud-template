package com.company.web.ket;

import java.util.Arrays;

public class LC122买卖股票的最佳时机4 {
	public static void main(String[] args) {
		int[] coins = new int[] { 3, 2, 5, 0, 0, 3, 1, 4 };
//		int[] coins = new int[] { 1, 2, 3, 4, 5 };
//		int[] coins = new int[] { 1, 2 };

		int coinChange = new LC122买卖股票的最佳时机4().maxProfit(coins, 2);
		System.out.println(coinChange);
	}

	public int maxProfit(int[] prices, int k) {
		int sum = 0;
		int s = 0;
		
		int[] pros = new int[prices.length];
		
		int j = 0;
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] < prices[i - 1]) {
				int maxProfit2 = maxProfit2(prices, s, i - 1);
				sum += maxProfit2;
				pros[j] = maxProfit2;
				j++;
				System.out.println("sum：" + sum + "  s:" + s + "  i-1:" + (i - 1) + "  i:" + (i));
				s = i;
			}
		}
		if (s < prices.length - 1) {
			int maxProfit2 = maxProfit2(prices, s, prices.length - 1);
			sum += maxProfit2;
			pros[j] = maxProfit2;
		}
		System.out.println(Arrays.toString(pros));
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