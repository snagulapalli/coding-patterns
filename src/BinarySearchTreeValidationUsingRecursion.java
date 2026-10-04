import java.util.LinkedList;
import java.util.Queue;

/*
 Time: O(n) - each node is visited at most once (short-circuit can stop early on invalid trees)
 Space: O(h) recursion stack - O(n) worst case for a skewed tree; O(log n) if balanced
 */
public class BinarySearchTreeValidationUsingRecursion {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public boolean isValidBST(TreeNode root) {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    boolean isValid(TreeNode node, long min, long max) {
        if (node == null) return true;

        if (node.val <= min || node.val >= max) return false;

        return isValid(node.left, min, node.val) && isValid(node.right, node.val, max);
    }

    // Builds a tree from LeetCode-style level-order input (null = missing child)
    static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) return null;

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

    static void runTest(String label, Integer[] values, boolean expected) {
        BinarySearchTreeValidationUsingRecursion solution = new BinarySearchTreeValidationUsingRecursion();
        boolean actual = solution.isValidBST(buildTree(values));
        String status = (actual == expected) ? "PASS" : "FAIL";
        System.out.printf("%-4s | %-40s | expected=%-5b actual=%b%n",
                status, label, expected, actual);
    }

    public static void main(String[] args) {
        // Examples from the problem
        runTest("Example 1: [2,1,3]", new Integer[]{2, 1, 3}, true);
        runTest("Example 2: [5,1,4,null,null,3,6]", new Integer[]{5, 1, 4, null, null, 3, 6}, false);
        runTest("Example 3: [5,4,6,null,null,3,7]", new Integer[]{5, 4, 6, null, null, 3, 7}, false);

        // Edge cases
        runTest("Single node", new Integer[]{1}, true);
        runTest("Integer.MAX_VALUE single node", new Integer[]{Integer.MAX_VALUE}, true);
        runTest("Integer.MIN_VALUE single node", new Integer[]{Integer.MIN_VALUE}, true);
        runTest("Duplicate value [2,2,2]", new Integer[]{2, 2, 2}, false);
        runTest("Valid larger BST", new Integer[]{8, 4, 12, 2, 6, 10, 14}, true);
        runTest("Left-skewed valid [3,2,null,1]", new Integer[]{3, 2, null, 1}, true);
    }
}
