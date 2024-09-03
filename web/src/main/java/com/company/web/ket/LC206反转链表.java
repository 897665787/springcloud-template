package com.company.web.ket;

public class LC206反转链表 {
	public static void main(String[] args) {
//		ListNode head = new ListNode(1, new ListNode(2, new ListNode(2, new ListNode(1, null))));
		ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null)))));
//		ListNode head = new ListNode(1, new ListNode(1, new ListNode(2, new ListNode(1, null))));
//		ListNode head = new ListNode(1, new ListNode(2, null));
		ListNode reverseList = new LC206反转链表().reverseList(head);
		while(reverseList != null) {
			System.out.println(reverseList.val);
			reverseList = reverseList.next;
		}
	}

	public ListNode reverseList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode curr = reverseList(head.next);
		head.next.next = head;
		head.next = null;
		return curr;
	}
}