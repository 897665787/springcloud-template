package com.company.web.ket;

import java.util.Arrays;

public class LC121买卖股票的最佳时机 {
	public static void main(String[] args) {
		int[] coins = new int[] { 7, 1, 5, 3, 6, 4 };
//		int[] coins = new int[] { 1, 2 };

		int coinChange = new LC121买卖股票的最佳时机().maxProfit(coins);
		System.out.println(coinChange);
	}

	public int maxProfit(int[] prices) {
		int[] max = new int[prices.length];
		max[0] = 0;
		int left_min = prices[0];
		for (int i = 1; i < prices.length; i++) {
			max[i] = Integer.max(prices[i] - left_min, max[i - 1]);
			if (left_min > prices[i]) {
				left_min = prices[i];
			}
//			System.out.println(Arrays.toString(max)+"   "+i + "  "+left_min);
		}
		return max[prices.length - 1];
	}
}