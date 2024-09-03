package com.company.web.ket;

public class NC109岛屿数量 {
	public static void main(String[] args) {
//		char[][] grid = new char[][] { { 1, 1 }, { 1, 1 } };
//		char[][] grid = new char[][] { { 1 } };
		char[][] grid = new char[][] { 
			{ 1, 1, 0, 0, 0 }, 
			{ 0, 1, 0, 1, 1 }, 
			{ 0, 0, 0, 1, 1 }, 
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 1, 1 } 
			};
			
		int lcs = new NC109岛屿数量().solve(grid);
		System.out.println(lcs);
	}

	/**
	 * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
	 *
	 * 判断岛屿数量
	 * 
	 * @param grid char字符型二维数组
	 * @return int整型
	 */
	public int solve(char[][] grid) {
		int cnt = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				int c = grid[i][j];
				if (c == '1' || c == '2') {
					if (c == '1') {
						cnt++;
					}
					grid[i][j] = '2';
					if (j < grid[i].length - 1 && grid[i][j + 1] == '1') {
						grid[i][j + 1] = '2';
					}
					if (i < grid.length - 1 && grid[i + 1][j] == '1') {
						grid[i + 1][j] = '2';
					}
				}
			}
		}

//		for (int i = 0; i < grid.length; i++) {
//			char[] dp2 = grid[i];
//			for (int j = 0; j < dp2.length; j++) {
//				System.out.print(String.valueOf(dp2[j]) + " ");
//			}
//			System.out.println();
//		}
		return cnt;
	}
}