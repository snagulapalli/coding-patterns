import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

// Time:  O(n) - every node visited only once
// Space: O(w) - queue holds at most one level (w = widest level, worst case ~n/2 => O(n))
public class MaximumWidthOfBinaryTree {

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

    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) return 0;
        record TreeNodeInfo(TreeNode treeNode, Integer index) {}
        Deque<TreeNodeInfo> queue = new LinkedList<>();
        int maxWidth = 0;
        queue.offer(new TreeNodeInfo(root, 0));
        while (!queue.isEmpty()) {
            int size = queue.size();
            maxWidth = Math.max(maxWidth,
                    Math.abs(queue.peekLast().index() - queue.peekFirst().index() + 1));
            for (int i = 0; i < size; i++) {
                TreeNodeInfo treeNodeInfo = queue.poll();
                if (treeNodeInfo.treeNode().left != null) {
                    queue.offer(new TreeNodeInfo(treeNodeInfo.treeNode().left,
                            treeNodeInfo.index() * 2));
                }
                if (treeNodeInfo.treeNode().right != null) {
                    queue.offer(new TreeNodeInfo(treeNodeInfo.treeNode().right,
                            treeNodeInfo.index() * 2 + 1));
                }
            }
        }
        return maxWidth;
    }

    // Builds a tree from LeetCode-style level-order input, e.g. {1, 3, 2, 5, null, null, 9}
    static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) return null;
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode current = queue.poll();
            if (i < values.length && values[i] != null) {
                current.left = new TreeNode(values[i]);
                queue.offer(current.left);
            }
            i++;
            if (i < values.length && values[i] != null) {
                current.right = new TreeNode(values[i]);
                queue.offer(current.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        MaximumWidthOfBinaryTree solution = new MaximumWidthOfBinaryTree();

        Integer[][] inputs = {
            {1, 3, 2, 5, 3, null, 9},
            {1, 3, 2, 5, null, null, 9, 6, null, 7},
            {1, 3, 2, 5},
            {1}                                   // extra: single node
        };
        int[] expected = {4, 7, 2, 1};

        for (int t = 0; t < inputs.length; t++) {
            int result = solution.widthOfBinaryTree(buildTree(inputs[t]));
            System.out.printf("Example %d: %s -> %d (expected %d) %s%n",
                    t + 1, java.util.Arrays.toString(inputs[t]), result, expected[t],
                    result == expected[t] ? "PASS" : "FAIL");
        }
    }
}
