package com.company.user.coupon.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class CollectionUtil {

	/**
	 * 求多个集合的交集
	 * 
	 * 参考 cn.hutool.core.collection.CollectionUtil.intersection 源码
	 * 
	 * @param colls
	 * @return
	 */
	public static <T> List<T> intersection(List<List<T>> colls) {
		if (colls.isEmpty()) {
			return Lists.newArrayList();
		}

		if (colls.size() == 1) {
			return colls.get(0);
		}

		Collection<T> firstProductCodeList = colls.get(0);

		for (int i = 1; i < colls.size(); i++) {
			firstProductCodeList = cn.hutool.core.collection.CollectionUtil.intersection(firstProductCodeList,
					colls.get(i));
			if (cn.hutool.core.collection.CollectionUtil.isEmpty(firstProductCodeList)) {
				return Lists.newArrayList();
			}
		}

		return firstProductCodeList.stream().collect(Collectors.toList());
	}

	public static void main(String[] args) {
		List<List<String>> list = Lists.newArrayList();

		List<String> list1 = Lists.newArrayList("1", "2", "5");
		list.add(list1);
		List<String> list2 = Lists.newArrayList("1", "2", "53");
		list.add(list2);
		List<String> list3 = Lists.newArrayList("1", "22", "2", "544");
		list.add(list3);

		System.out.println(CollectionUtil.intersection(list));
	}
}