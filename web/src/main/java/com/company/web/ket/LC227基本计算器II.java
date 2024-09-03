package com.company.web.ket;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LC227基本计算器II {
	public static void main(String[] args) {
		String s = "3+2*2";
		int calculate = new LC227基本计算器II().calculate(s);
		System.out.println(calculate);
	}

	public int calculate(String s) {
		s = s.replace(" ", "").trim();
		char[] chars = s.toCharArray();

		Stack<Character> stack = new Stack<>();
		List<Character> list = new ArrayList<>();

		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			list.add(c);

			Character pre_fuhao = stack.pop();
			if (pre_fuhao == null) {

			} else {
				Character pre_num = stack.pop();
				if (pre_fuhao == '*') {
					int aaa = Integer.valueOf(String.valueOf(pre_num)) * Integer.valueOf(String.valueOf(c));
					
				} else if (pre_fuhao == '/') {
					
				} else if (pre_fuhao == '+') {
					
				} else if (pre_fuhao == '-') {

				} else {
					stack.push(c);
					list.add(c);
				}
			}

			if (c == '*') {
				Character pre = stack.pop();
				int aaa = Integer.valueOf(String.valueOf(pre)) * Integer.valueOf(String.valueOf(chars[i + 1]));
				
			} else if (c == '/') {
				
			} else if (c == '+') {
				
			} else if (c == '-') {

			} else {
				stack.push(c);
				list.add(c);
			}
		}

		int res = 0;

		for (int i = 0; i < chars.length; i++) {

		}

		return 0;
	}
}