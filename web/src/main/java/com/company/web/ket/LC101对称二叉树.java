package com.company.web.ket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LC101对称二叉树 {
	public static void main(String[] args) {
//		TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(3, null, null), new TreeNode(4, null, null)),
//				new TreeNode(2, new TreeNode(4, null, null), new TreeNode(3, null, null)));
		TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(3, null, null), new TreeNode(4, 
				new TreeNode(8, null, null), new TreeNode(9, null, null))),
				new TreeNode(2, new TreeNode(4, null, null), new TreeNode(3, 
						new TreeNode(9, null, null), 
						new TreeNode(8, null, null))));
//		TreeNode root = new TreeNode(1, new TreeNode(0, null, null), null);
		boolean symmetric = new LC101对称二叉树().isSymmetric(root);
		System.out.println(symmetric);
	}

	public boolean isSymmetric(TreeNode root) {
		Queue<TreeNode> q = new LinkedList<>();
		q.offer(root);

		while (!q.isEmpty()) {
			List<TreeNode> list = new ArrayList<>();
			while (!q.isEmpty()) {
				list.add(q.poll());
			}

//			for (TreeNode treeNode : list) {
//				System.out.print(treeNode.val + " ");
//			}
//			System.out.println();

			boolean symmetricList = isSymmetricList(list);
			if (!symmetricList) {
				return false;
			}

			boolean hasOne = false;
			for (TreeNode treeNode : list) {
				if (treeNode.val != -101) {
					hasOne = true;
					break;
				}
			}
			if (hasOne) {

				for (TreeNode treeNode : list) {
					if (treeNode.val != -101) {
						if (treeNode.left == null) {
							q.offer(new TreeNode(-101));
						} else {
							q.offer(treeNode.left);
						}
						if (treeNode.right == null) {
							q.offer(new TreeNode(-101));
						} else {
							q.offer(treeNode.right);
						}
					}
				}
			}

		}
		return true;
	}

	public boolean isSymmetricList(List<TreeNode> list) {
		int i = 0;
		int j = list.size() - 1;

		for (int j2 = 0; j2 < list.size() / 2; j2++) {
			if (list.get(i).val != list.get(j).val) {
				return false;
			}
			i++;
			j--;
		}
		return true;
	}
}