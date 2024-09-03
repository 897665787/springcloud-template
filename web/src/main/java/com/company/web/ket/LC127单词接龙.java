package com.company.web.ket;

import java.util.List;

import com.google.common.collect.Lists;

public class LC127单词接龙 {
	public static void main(String[] args) {
		List<String> wordList = Lists.newArrayList("hot", "dot", "dog", "lot", "log", "cog");
		String beginWord = "hit";
		String endWord = "cog";
		int rob = new LC127单词接龙().ladderLength(beginWord, endWord, wordList);
		System.out.println(rob);
	}

	public int ladderLength(String beginWord, String endWord, List<String> wordList) {
		
		return 0;
	}
}