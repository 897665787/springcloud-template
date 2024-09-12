package com.company.web.ket;

public class LC124二叉树中的最大路径和 {
	public static void main(String[] args) {
//		TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(3, null, null), new TreeNode(4, null, null)),
//				new TreeNode(2, new TreeNode(4, null, null), new TreeNode(3, null, null)));
//		TreeNode root = new TreeNode(-10, 
//				new TreeNode(9, null, null),
//				new TreeNode(20, 
//						new TreeNode(15, null, null), 
//						new TreeNode(7, null, null))
//				);
		TreeNode root = new TreeNode(5, 
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
//		TreeNode root = new TreeNode(-10, 
//				null,
//				null
//				);
//		TreeNode root = new TreeNode(1, new TreeNode(0, null, null), null);
		int symmetric = new LC124二叉树中的最大路径和().maxPathSum(root);
		System.out.println(symmetric);
	}

	int max = Integer.MIN_VALUE;
	public int maxPathSum(TreeNode root) {
		maxPathSum2(root);
		return max;
	}

	public int maxPathSum2(TreeNode root) {
		if (root == null) {
			return 0;
		}

		int suml = 0;
		if (root.left != null) {
			suml = Integer.max(0, maxPathSum2(root.left));
		}
		int sumr = 0;
		if (root.right != null) {
			sumr = Integer.max(0, maxPathSum2(root.right));
		}
		int sum = root.val + suml + sumr;
//		System.out.println("sum:" + sum);
		if (max < sum) {
			max = sum;
		}
		return Integer.max(suml, sumr) + root.val;
	}
}