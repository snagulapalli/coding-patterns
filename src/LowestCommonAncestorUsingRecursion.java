import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 236 - Lowest Common Ancestor of a Binary Tree (recursive solution).
 *
 * Time:  O(n) - every node is visited at most once
 * Space: O(h) recursion stack, where h is the tree height;
 *        O(n) in the worst case (skewed tree), O(log n) if balanced
 */
public class LowestCommonAncestorUsingRecursion {

    /** Definition for a binary tree node. */
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        if (root == null || p == root || q == root) return root;

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null) return root;  // p and q split here

        if (left != null) return left;                   // pass up whatever was found
        return right;                                    // may be null: nothing found
    }

    // ---------- Test helpers ----------

    /** Builds a tree from LeetCode-style level-order input (null = missing child). */
    static TreeNode buildTree(Integer[] values) {
        if (values.length == 0 || values[0] == null) return null;
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode current = queue.poll();
            if (i < values.length && values[i] != null) {
                current.left = new TreeNode(values[i]);
                queue.add(current.left);
            }
            i++;
            if (i < values.length && values[i] != null) {
                current.right = new TreeNode(values[i]);
                queue.add(current.right);
            }
            i++;
        }
        return root;
    }

    /** Finds the node with the given value (values are unique per the constraints). */
    static TreeNode findNode(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;
        TreeNode found = findNode(root.left, val);
        return (found != null) ? found : findNode(root.right, val);
    }

    static void runTest(String name, Integer[] tree, int pVal, int qVal, int expected) {
        TreeNode root = buildTree(tree);
        TreeNode p = findNode(root, pVal);
        TreeNode q = findNode(root, qVal);
        TreeNode result = new LowestCommonAncestorUsingRecursion().lowestCommonAncestor(root, p, q);
        String status = (result != null && result.val == expected) ? "PASS" : "FAIL";
        System.out.printf("%s: p=%d, q=%d -> LCA=%s (expected %d) %s%n",
                name, pVal, qVal, result == null ? "null" : result.val, expected, status);
    }

    public static void main(String[] args) {
        Integer[] tree = {3, 5, 1, 6, 2, 0, 8, null, null, 7, 4};

        runTest("Example 1", tree, 5, 1, 3);
        runTest("Example 2", tree, 5, 4, 5);
        runTest("Example 3", new Integer[]{1, 2}, 1, 2, 1);

        // Extra cases
        runTest("Deep split", tree, 7, 4, 2);
        runTest("Cousins  ", tree, 6, 4, 5);
        runTest("Far apart", tree, 7, 8, 3);
    }
}
