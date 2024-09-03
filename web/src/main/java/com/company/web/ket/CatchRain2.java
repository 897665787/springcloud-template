package com.company.web.ket;

import com.company.common.util.JsonUtil;

public class CatchRain2 {
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

		int[] max_left = new int[arr.length];
		int[] max_right = new int[arr.length];

		for (int i = 1; i < arr.length - 1; i++) {
			max_left[i] = Math.max(max_left[i - 1], arr[i - 1]);
		}
		System.out.println("max_left:" + JsonUtil.toJsonString(max_left));
		for (int i = arr.length - 2; i > 0; i--) {
			max_right[i] = Math.max(max_right[i + 1], arr[i + 1]);
		}
		System.out.println("max_right:" + JsonUtil.toJsonString(max_right));

		int sum = 0;
		for (int i = 1; i < arr.length - 1; i++) {
			int min = Math.min(max_left[i], max_right[i]);
			if (min > arr[i]) {
				sum += min - arr[i];
			}
		}

		System.out.println(sum);
	}
}