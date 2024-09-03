package com.company.web.ket;

public class LC322零钱兑换 {
	public static void main(String[] args) {
		int[] coins = new int[] { 1, 2, 5 };

		int coinChange = new LC322零钱兑换().coinChange(coins, 50);

		System.out.println(coinChange);
	}

	int[] leftamounts;
	public int coinChange(int[] coins, int amount) {
		leftamounts = new int[amount];
		return coinChange2(coins, amount);
	}

	public int coinChange2(int[] coins, int amount) {
		if (amount < 0) {
			return -1;
		}
		if (amount == 0) {
			return 0;
		}

		if (leftamounts[amount - 1] != 0) {
			return leftamounts[amount - 1];
		}

		int min = Integer.MAX_VALUE;
		for (int i = 0; i < coins.length; i++) {
			int leftamount = amount - coins[i];
			int coinChange = coinChange2(coins, leftamount);
			if (coinChange != -1) {
				min = Integer.min(min, coinChange + 1);
			}
		}
		min = min == Integer.MAX_VALUE ? -1 : min;
		leftamounts[amount - 1] = min;
		return min;
	}
}