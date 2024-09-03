package com.company.web.ket;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

public class NC97字符串出现次数的TopK问题 {
	public static void main(String[] args) {
		String[] strings = new String[] { "a", "b", "c", "b" };

		String[][] topKstrings = new NC97字符串出现次数的TopK问题().topKstrings(strings, 2);
		System.out.println(topKstrings);
	}

	public String[][] topKstrings(String[] strings, int k) {
		Map<String, Integer> map = new HashMap<>();
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			Integer orDefault = map.getOrDefault(string, 0);
			map.put(string, orDefault + 1);
		}

		PriorityQueue<Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> {
			int dd = b.getValue() - a.getValue();
			if (dd == 0) {
				dd = a.getKey().compareTo(b.getKey());
			}
			return dd;
		});
		Set<Entry<String, Integer>> entrySet = map.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			pq.offer(entry);
		}

		String[][] result = new String[k][2];
		int i = 0;
		while (!pq.isEmpty()) {
			Entry<String, Integer> poll = pq.poll();
			String[] result2 = new String[2];
			result2[0] = poll.getKey();
			result2[1] = poll.getValue() + "";
			result[i] = result2;
			i++;
			if (i == k) {
				break;
			}
		}
		return result;
	}
}