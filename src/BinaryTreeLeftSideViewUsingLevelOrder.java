import java.util.*;

/**
 * Binary Tree Left Side View — level-order (BFS) approach.
 *
 * Time:  O(n) — each node is offered to and polled from the queue exactly once.
 * Space: O(w) — w is the maximum width of the tree (largest level); this is O(n)
 *        in the worst case, e.g. ~n/2 nodes on the last level of a complete tree.
 *        Excludes the output list.
 */
public class BinaryTreeLeftSideViewUsingLevelOrder {

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

    public List<Integer> leftSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int queueSize = queue.size();
            for (int i = 0; i < queueSize; i++) {
                TreeNode node = queue.poll();
                if (i == 0) result.add(node.val);   // first node of each level

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }

    /** Builds a tree from LeetCode-style level-order input, e.g. {1, 2, 3, null, 5, null, 4}. */
    static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) return null;

        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();
            if (i < values.length && values[i] != null) {
                node.left = new TreeNode(values[i]);
                queue.offer(node.left);
            }
            i++;
            if (i < values.length && values[i] != null) {
                node.right = new TreeNode(values[i]);
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }

    private static int passed = 0;
    private static int failed = 0;

    private static void runTest(String name, Integer[] input, List<Integer> expected) {
        BinaryTreeLeftSideViewUsingLevelOrder solver = new BinaryTreeLeftSideViewUsingLevelOrder();
        List<Integer> actual = solver.leftSideView(buildTree(input));
        boolean ok = actual.equals(expected);
        if (ok) passed++; else failed++;
        System.out.printf("%-40s input=%-36s expected=%-14s actual=%-14s %s%n",
                name, Arrays.toString(input), expected, actual, ok ? "PASS" : "FAIL");
    }

    public static void main(String[] args) {
        // Right-side-view LeetCode inputs, with left-side-view expectations
        runTest("Example 1", new Integer[]{1, 2, 3, null, 5, null, 4}, List.of(1, 2, 5));
        runTest("Example 2", new Integer[]{1, null, 3}, List.of(1, 3));
        runTest("Example 3 (empty tree)", new Integer[]{}, List.of());

        // Edge cases
        runTest("Single node", new Integer[]{7}, List.of(7));
        runTest("Left-skewed", new Integer[]{1, 2, null, 3, null, 4}, List.of(1, 2, 3, 4));
        runTest("Right-skewed (right nodes visible)", new Integer[]{1, null, 2, null, 3}, List.of(1, 2, 3));
        runTest("Deeper right subtree shows through", new Integer[]{1, 2, 3, null, null, null, 5}, List.of(1, 2, 5));
        runTest("Complete tree", new Integer[]{1, 2, 3, 4, 5, 6, 7}, List.of(1, 2, 4));
        runTest("Negative values", new Integer[]{-1, -2, -3, null, -100}, List.of(-1, -2, -100));

        System.out.printf("%n%d passed, %d failed%n", passed, failed);
    }
}
