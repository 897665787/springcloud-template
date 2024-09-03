package com.company.web.ket;

import java.util.Arrays;

public class LC912排序 {
	public static void main(String[] args) {
		int[] nums = new int[] { 5, 2, 3, 1 };
//		int[] nums = new int[] { 110, 100, 0 };
		int[] sortArray = new LC912排序().sortArray(nums);
		System.out.println(Arrays.toString(sortArray));
	}

	public int[] sortArray(int[] nums) {
		qsort(nums, 0, nums.length - 1);
		return nums;
	}

	public void qsort(int[] nums, int l, int r) {
		if (l > r) {
			return;
		}
		int mid = findmid(nums, l, r);
		qsort(nums, l, mid - 1);
		qsort(nums, mid + 1, r);
	}

	public int findmid(int[] nums, int l, int r) {
		int lp = l;
		int rp = r;
		int temp = nums[l];

		while (lp < rp) {
			while (lp < rp && nums[rp] > temp) {
				rp--;
			}

			while (lp < rp && nums[lp] <= temp) {
				lp++;
			}

			if (lp < rp) {
				int tval = nums[rp];
				nums[rp] = nums[lp];
				nums[lp] = tval;
			}
		}

		nums[l] = nums[rp];
		nums[rp] = temp;
		return rp;
	}
}