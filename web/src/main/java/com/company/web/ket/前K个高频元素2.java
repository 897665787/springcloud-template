package com.company.web.ket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import com.company.common.util.JsonUtil;

public class 前K个高频元素2 {
	public static void main(String[] args) {
//		int[] arr = new int[] { 1, 1, 1, 2, 2, 3 };
//		int[] arr = new int[] { 3, 0, 1, 0 };
		int[] arr = new int[] { 4, 1, -1, 2, -1, 2, 3 };

		List<Integer> topKFrequent = new 前K个高频元素2().topKFrequent(arr, 2);
		System.out.println(JsonUtil.toJsonString(topKFrequent));
	}

	public List<Integer> topKFrequent(int[] nums, int k) {
        // 使用字典，统计每个元素出现的次数，元素为键，元素出现的次数为值
        HashMap<Integer,Integer> map = new HashMap();
        for(int num : nums){
            if (map.containsKey(num)) {
               map.put(num, map.get(num) + 1);
             } else {
                map.put(num, 1);
             }
        }
        // 遍历map，用最小堆保存频率最大的k个元素
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return map.get(a) - map.get(b);
            }
        });
        for (Integer key : map.keySet()) {
            if (pq.size() < k) {
                pq.add(key);
            } else if (map.get(key) > map.get(pq.peek())) {
                pq.remove();
                pq.add(key);
            }
        }
        // 取出最小堆中的元素
        List<Integer> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            res.add(pq.remove());
        }
        return res;
    }
}