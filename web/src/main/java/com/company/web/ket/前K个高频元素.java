package com.company.web.ket;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import com.company.common.util.JsonUtil;

public class 前K个高频元素 {
	public static void main(String[] args) {
//		int[] arr = new int[] { 1, 1, 1, 2, 2, 3 };
//		int[] arr = new int[] { 3, 0, 1, 0 };
		int[] arr = new int[] { 4, 1, -1, 2, -1, 2, 3 };

		int[] topKFrequent = new 前K个高频元素().topKFrequent(arr, 2);
		System.out.println(JsonUtil.toJsonString(topKFrequent));
	}

	public int[] topKFrequent(int[] nums, int k) {
		Map<Integer, Integer> map = new HashMap<>();

		for (int i = 0; i < nums.length; i++) {
			int num = nums[i];
			Integer val = map.getOrDefault(num, 0);
			map.put(num, val + 1);
		}
//		System.out.println(JsonUtil.toJsonString(map));

		PriorityQueue<Integer> dump = new PriorityQueue<>((a, b) -> map.get(a) - map.get(b));

		Set<Entry<Integer, Integer>> entrySet = map.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();

			if (dump.size() < k) {
				dump.add(key);
			} else {
				Integer peekval = map.get(dump.peek());
				if (peekval < value) {
					dump.poll();
					dump.add(key);
				}
			}
//			System.out.println(JsonUtil.toJsonString(dump));
		}
//		System.out.println(JsonUtil.toJsonString(dump));
		int[] toplist = new int[k];
		for (int i = 0; i < k; i++) {
			toplist[i] = dump.poll();
		}
		return toplist;
	}
}