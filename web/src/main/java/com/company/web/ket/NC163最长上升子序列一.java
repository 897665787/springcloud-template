package com.company.web.ket;

import java.util.Arrays;

public class NC163最长上升子序列一 {
	public static void main(String[] args) {
//		int[] arr = new int[] { 6, 3, 1, 5, 2, 3, 7 };
		int[] arr = new int[] {1,2,3,4,5,6};
		int lcs = new NC163最长上升子序列一().LIS(arr);
		System.out.println(lcs);
	}
	
    public int LIS (int[] arr) {
    	return LIS2(arr, arr);
    }
    
	public int LIS2(int[] charArray1, int[] charArray2) {
		int length1 = charArray1.length;
		int length2 = charArray2.length;

		int[][] dp = new int[length1 + 1][length2 + 1];
		
		for (int i = 1; i <= length1; i++) {
			int c1 = charArray1[i - 1];
			dp[i - 1][i - 1] = 1;
			int last = c1;
			for (int j = i; j <= length2; j++) {
				int c2 = charArray2[j - 1];
				if (last < c2) {
					dp[i][j] = dp[i][j - 1] + 1;
					last = c2;
				} else {
//					dp[i][j] = Integer.max(dp[i - 1][j], dp[i][j - 1]);
					dp[i][j] = dp[i][j - 1];
				}
			}
		}
		
		for (int i = 0; i < dp.length; i++) {
			int[] dp2 = dp[i];
			for (int j = 0; j < dp2.length; j++) {
				System.out.print(dp2[j]+" ");
			}
			System.out.println();
		}
		
		return dp[length1][length2];
	}
}