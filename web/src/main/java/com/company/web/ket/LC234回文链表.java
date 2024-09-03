package com.company.web.ket;

public class LC234回文链表 {
	public static void main(String[] args) {
//		ListNode head = new ListNode(1, new ListNode(2, new ListNode(2, new ListNode(1, null))));
//		ListNode head = new ListNode(1, new ListNode(2, new ListNode(2, new ListNode(2, new ListNode(1, null)))));
		ListNode head = new ListNode(1, new ListNode(1, new ListNode(2, new ListNode(1, null))));
//		ListNode head = new ListNode(1, new ListNode(2, null));
		boolean palindrome = new LC234回文链表().isPalindrome(head);
		System.out.println(palindrome);
	}

	boolean isp = true;

	public boolean isPalindrome(ListNode head) {
		if (head == null) {
			return true;
		}

		ListNode p = head;
		ListNode pe = head;
		while (pe != null && pe.next != null) {
			p = p.next;
			pe = pe.next.next;
		}
		if (pe != null) {
			p = p.next;
		}
		isPalindrome2(head, p);
		return isp;
	}

	public ListNode isPalindrome2(ListNode ps, ListNode pe) {
		if (pe == null) {
			return ps;
		}
		ListNode ps2 = isPalindrome2(ps, pe.next);
		if (!isp) {
			return ps2.next;
		}
		if (ps2.val != pe.val) {
			isp = false;
		}
		return ps2.next;
	}
}