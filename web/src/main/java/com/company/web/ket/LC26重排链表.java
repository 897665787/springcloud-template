package com.company.web.ket;

public class LC26重排链表 {
	public static void main(String[] args) {
		ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));
//		ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null)))));
		new LC26重排链表().print(head);
		new LC26重排链表().reorderList(head);
		new LC26重排链表().print(head);
	}

	public void reorderList(ListNode head) {
		ppre = head;
		ListNode plast = head;
		reorderList2(plast);
	}

	int cnt;
	ListNode ppre;

	public ListNode reorderList2(ListNode plast) {
		cnt++;
		if (plast == null || plast.next == null) {
			return plast;
		}
		ListNode last = reorderList2(plast.next);
//		System.out.println(ppre.val + "  " + plast.val + "  " + last.val);
		cnt -= 2;
		if (cnt <= 0) {
			return plast;
		}

		ListNode pprenext = ppre.next;
		ppre.next = last;
		last.next = pprenext;
		plast.next = null;
//		new LC26重排链表().print(head0);
		ppre = pprenext;
		return plast;
	}

	void print(ListNode head) {
		System.out.print("print: ");
		while (head != null) {
			System.out.print(head.val + " ");
			head = head.next;
		}
		System.out.println();
	}
}