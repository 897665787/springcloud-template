package com.company.web.ket;

public class LC二叉树A是否包含二叉树B {
	public static void main(String[] args) {
//		TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(3, null, null), new TreeNode(4, null, null)),
//				new TreeNode(2, new TreeNode(4, null, null), new TreeNode(3, null, null)));
//		TreeNode root = new TreeNode(-10, 
//				new TreeNode(9, null, null),
//				new TreeNode(20, 
//						new TreeNode(15, null, null), 
//						new TreeNode(7, null, null))
//				);
		TreeNode root1 = new TreeNode(5, 
				new TreeNode(4, 
						new TreeNode(11, 
								new TreeNode(7, null, null), 
								new TreeNode(2, null, null)),
						null),
				new TreeNode(8, 
						new TreeNode(13, null, null), 
						new TreeNode(4, 
								null, 
								new TreeNode(1, null, null)))
				);
//		TreeNode root2 = new TreeNode(8, 
//				new TreeNode(13, null, null),
//				new TreeNode(4, null, null)
//				);
//		TreeNode root2 = new TreeNode(8, 
//				null,
//				new TreeNode(4, 
//						null, 
//						new TreeNode(1, null, null))
//				);
		TreeNode root2 = new TreeNode(5, 
				new TreeNode(4, 
						null,
						null),
				new TreeNode(8, 
						null, 
						null)
				);
//		TreeNode root = new TreeNode(1, new TreeNode(0, null, null), null);
		
		boolean symmetric = new LC二叉树A是否包含二叉树B().isSubtree(root1, root2);
		System.out.println(symmetric);
	}

	boolean hasY = false;
	public boolean isSubtree(TreeNode root, TreeNode subRoot) {
		if (root == null || subRoot == null) {
			return false;
		}
		bianli(root, subRoot);
		return hasY;
    }
	
	public void bianli(TreeNode node, TreeNode root2) {
		if (node == null) {
			return;
		}
		boolean same2 = same2(node, root2);
//		System.out.println("node.val:" + node.val + "  same2:" + same2);
		if (same2) {
			hasY = true;
			return;
		}
		if (node.left != null) {
			bianli(node.left, root2);
		}
		if (node.right != null) {
			bianli(node.right, root2);
		}
	}
	
	public boolean same2(TreeNode root1, TreeNode root2) {
		if (root1 == null && root2 == null) {
			return true;
		}
//		System.out.println("root1.val:" + (root1 == null? null:root1.val) 
//				+ "         root2.val:" + (root2 == null? null:root2.val)
//				);
		if (root1 == null || root2 == null) {
			return false;
		}
		if (root1.val != root2.val) {
			return false;
		}
		if (root2.left != null) {
			boolean same = same2(root1.left, root2.left);
			if (!same) {
				return false;
			}
		}
		if (root2.right != null) {
			boolean same = same2(root1.right, root2.right);
			if (!same) {
				return false;
			}
		}
		return true;
	}
}