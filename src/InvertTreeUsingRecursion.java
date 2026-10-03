import java.util.*;

// LeetCode 226 - Invert Binary Tree
// Time:  O(n) — each node visited once
// Space: O(h) — recursion stack; O(log n) balanced, O(n) worst case (skewed)
public class InvertTreeUsingRecursion {

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

    static class Solution {
        public TreeNode invertTree(TreeNode root) {
            if (root == null) return null;

            TreeNode tmp = root.left;   // swap first (pre-order)
            root.left = root.right;
            root.right = tmp;

            invertTree(root.left);
            invertTree(root.right);
            return root;
        }
    }

    // ---------- Test harness ----------

    // Build a tree from LeetCode-style level-order array (null = missing node)
    static TreeNode build(Integer[] arr) {
        if (arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Deque<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode node = q.poll();
            if (i < arr.length && arr[i] != null) {
                node.left = new TreeNode(arr[i]);
                q.offer(node.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                node.right = new TreeNode(arr[i]);
                q.offer(node.right);
            }
            i++;
        }
        return root;
    }

    // Serialize to LeetCode-style level-order list, trimming trailing nulls
    static List<Integer> serialize(TreeNode root) {
        List<Integer> out = new ArrayList<>();
        if (root == null) return out;
        Queue<TreeNode> q = new LinkedList<>();   // LinkedList allows nulls
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node == null) {
                out.add(null);
                continue;
            }
            out.add(node.val);
            q.offer(node.left);
            q.offer(node.right);
        }
        while (!out.isEmpty() && out.get(out.size() - 1) == null) {
            out.remove(out.size() - 1);
        }
        return out;
    }

    static int passed = 0, failed = 0;

    static void check(String name, Integer[] input, Integer[] expected) {
        TreeNode result = new Solution().invertTree(build(input));
        List<Integer> actual = serialize(result);
        List<Integer> exp = Arrays.asList(expected);
        boolean ok = actual.equals(exp);
        if (ok) passed++; else failed++;
        System.out.printf("%s %-28s input=%s  expected=%s  actual=%s%n",
                ok ? "PASS" : "FAIL", name,
                Arrays.toString(input), exp, actual);
    }

    public static void main(String[] args) {
        check("Example 1", new Integer[]{4, 2, 7, 1, 3, 6, 9}, new Integer[]{4, 7, 2, 9, 6, 3, 1});
        check("Example 2", new Integer[]{2, 1, 3},             new Integer[]{2, 3, 1});
        check("Example 3 (empty)", new Integer[]{},            new Integer[]{});
        check("Single node", new Integer[]{1},                 new Integer[]{1});
        check("Left-skewed", new Integer[]{1, 2, null, 3},     new Integer[]{1, null, 2, null, 3});
        check("Right-skewed", new Integer[]{1, null, 2, null, 3}, new Integer[]{1, 2, null, 3});
        check("Negative values", new Integer[]{-1, -100, 100}, new Integer[]{-1, 100, -100});
        check("Sparse", new Integer[]{1, 2, 3, null, 4},       new Integer[]{1, 3, 2, null, null, 4});

        // Double inversion should restore the original tree
        Integer[] orig = {5, 3, 8, 1, 4, 7, 9};
        Solution s = new Solution();
        List<Integer> roundTrip = serialize(s.invertTree(s.invertTree(build(orig))));
        boolean ok = roundTrip.equals(Arrays.asList(orig));
        if (ok) passed++; else failed++;
        System.out.printf("%s %-28s%n", ok ? "PASS" : "FAIL", "Invert twice = original");

        System.out.printf("%nResults: %d passed, %d failed%n", passed, failed);
    }
}
