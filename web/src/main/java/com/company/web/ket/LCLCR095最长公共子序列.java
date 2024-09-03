package com.company.web.ket;

public class LCLCR095最长公共子序列 {
	public static void main(String[] args) {
		String text1 = "abcdefg";
		String text2 = "bcdeefg";
		int calculate = new LCLCR095最长公共子序列().longestCommonSubsequence(text1, text2);
		System.out.println(calculate);
	}

	public int longestCommonSubsequence(String text1, String text2) {
		char[] charArray1 = text1.toCharArray();
		char[] charArray2 = text2.toCharArray();

		int length1 = charArray1.length;
		int length2 = charArray2.length;

		int[][] dp = new int[length1 + 1][length2 + 1];
		
		int max = 0;
		for (int i = 1; i <= length1; i++) {
			char c1 = charArray1[i - 1];
			for (int j = 1; j <= length2; j++) {
				char c2 = charArray2[j - 1];
				if (c1 == c2) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
					if(dp[i][j] > max) {
						max = dp[i][j];
					}
				} else {
//					dp[i][j] = Integer.max(dp[i - 1][j], dp[i][j - 1]);
					dp[i][j] = 0;
				}
			}
		}

//		for (int i = 0; i < dp.length; i++) {
//			int[] dp2 = dp[i];
//			for (int j = 0; j < dp2.length; j++) {
//				System.out.print(dp2[j]+" ");
//			}
//			System.out.println();
//		}
		
		return max;
	}
}