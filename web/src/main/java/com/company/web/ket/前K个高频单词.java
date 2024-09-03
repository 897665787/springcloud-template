package com.company.web.ket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import com.company.common.util.JsonUtil;

public class 前K个高频单词 {
	public static void main(String[] args) {
//		String[] words = new String[] {"i", "love", "leetcode", "i", "love", "coding"};
//		String[] words = new String[] {"the","day","is","sunny","the","the","the","sunny","is","is"};
		String[] words = new String[] { "i", "love", "leetcode", "i", "love", "coding" };

		List<String> topKFrequent = new 前K个高频单词().topKFrequent(words, 1);
		System.out.println(JsonUtil.toJsonString(topKFrequent));
	}

    public List<String> topKFrequent(String[] words, int k) {
    	Map<String, Integer> map = new HashMap<>();

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			Integer val = map.getOrDefault(word, 0);
			map.put(word, val + 1);
		}
//		System.out.println(JsonUtil.toJsonString(map));

		PriorityQueue<String> dump = new PriorityQueue<>((a, b) -> {
			int val = map.get(a).compareTo(map.get(b));
			if (val == 0) {
				val = b.compareTo(a);
			}
			return val;
		});
		
		Set<Entry<String, Integer>> entrySet = map.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			String key = entry.getKey();

			if (dump.size() < k) {
				dump.add(key);
			} else {
				String peek = dump.peek();
				Integer peekval = map.get(peek);
				Integer val = map.get(key);
				if (peekval == val) {
					if (peek.compareTo(key) > 0) {
						dump.poll();
						dump.add(key);
					}
				} else if (peekval < val) {
					dump.poll();
					dump.add(key);
				}
			}
//			System.out.println(JsonUtil.toJsonString(dump));
		}
//		System.out.println(JsonUtil.toJsonString(dump));
		List<String> toplist = new ArrayList<String>();
		while (!dump.isEmpty()) {
			toplist.add(dump.poll());
		}
		toplist.sort((a, b) -> {
			int val = map.get(b).compareTo(map.get(a));
			if (val == 0) {
				val = a.compareTo(b);
			}
			return val;
		});
		return toplist;
	}
}