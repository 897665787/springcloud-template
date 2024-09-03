package com.company.web.ket;

public class LC198打家劫舍 {
	public static void main(String[] args) {
//		int[] nums = new int[] { 1, 2, 3, 1 };
		int[] nums = new int[] { 2, 1, 1, 2 };
		int rob = new LC198打家劫舍().rob(nums);
		System.out.println(rob);
	}

	public int rob(int[] nums) {
		if (nums.length == 1) {
			return nums[0];
		}
		int[] max = new int[nums.length];
		max[0] = nums[0];
		max[1] = Integer.max(max[0], nums[1]);
		for (int i = 2; i < nums.length; i++) {
			max[i] = Integer.max(max[i - 1], max[i - 2] + nums[i]);
//			System.out.println(Arrays.toString(max));
		}
		return max[nums.length - 1];
	}
}