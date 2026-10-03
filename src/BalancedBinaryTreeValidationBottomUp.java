import java.util.LinkedList;
import java.util.Queue;

// Time:  O(n) — each node visited exactly once (bottom-up, -1 sentinel short-circuits)
// Space: O(h) recursion stack — O(log n) balanced, O(n) skewed
public class BalancedBinaryTreeValidationBottomUp {

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

    public boolean isBalanced(TreeNode root) {
        if (root == null) return true;
        return getHeight(root) != -1;
    }

    int getHeight(TreeNode root) {
        if (root == null) return 0;

        int heightLeft = getHeight(root.left);
        if (heightLeft == -1) return -1;

        int heightRight = getHeight(root.right);
        if (heightRight == -1) return -1;

        if (Math.abs(heightLeft - heightRight) > 1) return -1;

        return 1 + Math.max(heightLeft, heightRight);
    }

    // Builds a tree from LeetCode-style level-order input (null = missing child).
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

    private static String format(Integer[] values) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(values[i]);
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        BalancedBinaryTreeValidationBottomUp solution = new BalancedBinaryTreeValidationBottomUp();

        Integer[][] inputs = {
            {3, 9, 20, null, null, 15, 7},                     // Example 1
            {1, 2, 2, 3, 3, null, null, 4, 4},                 // Example 2
            {},                                                // Example 3: empty tree
            {1, 2, 2, 3, null, null, 3, 4, null, null, 4},     // root balanced, subtrees not
            {1},                                               // single node
            {1, null, 2, null, 3}                              // right-skewed chain
        };
        boolean[] expected = {true, false, true, false, true, false};

        int passed = 0;
        for (int t = 0; t < inputs.length; t++) {
            boolean actual = solution.isBalanced(buildTree(inputs[t]));
            boolean ok = actual == expected[t];
            if (ok) passed++;
            System.out.printf("%-45s expected=%-5b actual=%-5b %s%n",
                    format(inputs[t]), expected[t], actual, ok ? "PASS" : "FAIL");
        }
        System.out.printf("%n%d/%d tests passed%n", passed, inputs.length);
    }
}
