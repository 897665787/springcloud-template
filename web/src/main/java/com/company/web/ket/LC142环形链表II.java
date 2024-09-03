package com.company.web.ket;

public class LC142环形链表II {
	public static void main(String[] args) {
//		ListNode listNode4 = new ListNode(-4, null);
//		ListNode listNode2 = new ListNode(2, new ListNode(0, listNode4));
//		ListNode head = new ListNode(3, listNode2);
//		listNode4.next = listNode2;
		
		ListNode listNode2 = new ListNode(2, null);
		ListNode head = new ListNode(1, listNode2);
		listNode2.next = head;
		
		ListNode reverseList = new LC142环形链表II().detectCycle(head);
		System.out.println(reverseList.val);
	}

	public ListNode detectCycle(ListNode head) {
		
		ListNode slow = head;
		ListNode fast = head;

		ListNode h = null;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			
			if(fast.val == slow.val) {
				h = fast;
				break;
			}
		}

		if (h == null) {// 无环
			return null;
		}
//		System.out.println("h:" + h.val);
		
		ListNode f = head;
		
		while (true) {
			if(f.val == h.val) {
				return f;
			}
			
			h = h.next;
			f = f.next;
		}
	}
}