package com.company.web.ket;

import com.company.common.util.JsonUtil;

public class CatchRain {
	public static void main(String[] args) {
		String value = "0,1,0,2,1,0,1,3,2,1,2,1";// 6
//		String value = "0,1,0,2,1,0,1,3,2,1,2";// 6
//		String value = "0,1,0,2,1,0,1,3,2,1";// 5
//		String value = "0,1,0,2,1,0,1";// 2

		String[] split = value.split(",");
		int[] arr = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			arr[i] = Integer.parseInt(split[i]);
		}
		System.out.println(JsonUtil.toJsonString(arr));
		
		int count = new CatchRain().trap(arr);
		System.out.println(count);
	}
	
    public int trap(int[] arr) {
		int maxheigt = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] >= maxheigt) {
				maxheigt = arr[i];
			}
		}
		int count = 0;
		for (int heigt = maxheigt; heigt > 0; heigt--) {
			int left = -1;
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] >= heigt) {
					left = i;
					break;
				}
			}
			if (left == -1) {
				continue;
			}
			int right = -1;
			for (int i = arr.length - 1; i >= 0; i--) {
				if (arr[i] >= heigt) {
					right = i;
					break;
				}
			}
			if (left == right) {
				continue;
			}

			int countlr = 0;
			for (int i = left + 1; i < right; i++) {
				if (arr[i] >= heigt) {
					countlr++;
				}
			}

			int r_l = right - left - 1 - countlr;
			count += r_l;
		}

		return count;
    }
}