package com.company.web.ket;

public class NC127最长公共子串 {
	public static void main(String[] args) {
		String text1 = "abcdefg";
		String text2 = "bcdeefg";
		String lcs = new NC127最长公共子串().LCS(text1, text2);
		System.out.println(lcs);
	}
	
	public String LCS(String str1, String str2) {
		char[] charArray1 = str1.toCharArray();
		char[] charArray2 = str2.toCharArray();

		int length1 = charArray1.length;
		int length2 = charArray2.length;

		int[][] dp = new int[length1 + 1][length2 + 1];
		
		int max = 0;
		int idj = 0;
		for (int i = 1; i <= length1; i++) {
			char c1 = charArray1[i - 1];
			for (int j = 1; j <= length2; j++) {
				char c2 = charArray2[j - 1];
				if (c1 == c2) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
					if(dp[i][j] > max) {
						max = dp[i][j];
						idj = j;
					}
				} else {
//					dp[i][j] = Integer.max(dp[i - 1][j], dp[i][j - 1]);
					dp[i][j] = 0;
				}
			}
		}
		
		
//		System.out.println("   "+idj);
//		for (int i = 0; i < dp.length; i++) {
//			int[] dp2 = dp[i];
//			for (int j = 0; j < dp2.length; j++) {
//				System.out.print(dp2[j]+" ");
//			}
//			System.out.println();
//		}
		
		return str2.substring(idj-max,idj);
	}
}