package com.company.web.ket;

public class LC236二叉树的最近公共祖先 {
	public static void main(String[] args) {
		TreeNode p = new TreeNode(5, new TreeNode(6, null, null),
				new TreeNode(2, new TreeNode(7, null, null), new TreeNode(4, null, null)));

		TreeNode q = new TreeNode(1, new TreeNode(0, null, null), new TreeNode(8, null, null));

		TreeNode root = new TreeNode(3, p, q);

		TreeNode symmetric = new LC236二叉树的最近公共祖先().lowestCommonAncestor(root, p, q);
		System.out.println(symmetric.val);
	}

	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || root == p || root == q) {
			return root;
		}

		TreeNode rootp = lowestCommonAncestor(root.left, p, q);
		TreeNode rootq = lowestCommonAncestor(root.right, p, q);

		if (rootp == null) {
			return rootq;
		}

		if (rootq == null) {
			return rootp;
		}
		return root;
	}
}