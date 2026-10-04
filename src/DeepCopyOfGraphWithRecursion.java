import java.util.*;

// Definition for a Node (matches LeetCode's provided class).
class Node {
    public int val;
    public List<Node> neighbors;

    public Node() {
        val = 0;
        neighbors = new ArrayList<>();
    }

    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<>();
    }

    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}

// Time:  O(V + E) - each node is cloned once (V), and each adjacency entry is
//        processed once (2E for an undirected graph).
// Space: O(V) auxiliary - visitedMap holds V entries, and the recursion stack can
//        go V deep (e.g., a long chain). The cloned graph itself is O(V + E),
//        but that is the required output.
class Solution {
    Map<Node, Node> visitedMap = new HashMap<>();

    public Node cloneGraph(Node node) {
        if (node == null) return null;

        Node clone = new Node(node.val);
        visitedMap.put(node, clone);   // register BEFORE recursing (stops cycles)

        for (Node neighbor : node.neighbors) {
            Node clonedNeighbor;
            if (visitedMap.containsKey(neighbor)) {
                clonedNeighbor = visitedMap.get(neighbor);
            } else {
                clonedNeighbor = cloneGraph(neighbor);
            }
            clone.neighbors.add(clonedNeighbor);
        }
        return clone;
    }
}

public class DeepCopyOfGraphWithRecursion {

    // Build a graph from a 1-indexed adjacency list; returns node 1 (or null if empty).
    static Node buildGraph(int[][] adjList) {
        if (adjList.length == 0) return null;
        Node[] nodes = new Node[adjList.length + 1];
        for (int i = 1; i <= adjList.length; i++) nodes[i] = new Node(i);
        for (int i = 1; i <= adjList.length; i++) {
            for (int nb : adjList[i - 1]) nodes[i].neighbors.add(nodes[nb]);
        }
        return nodes[1];
    }

    // Collect every node reachable from start (BFS), keyed by val.
    static Map<Integer, Node> collect(Node start) {
        Map<Integer, Node> byVal = new TreeMap<>();
        if (start == null) return byVal;
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(start);
        byVal.put(start.val, start);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            for (Node nb : cur.neighbors) {
                if (!byVal.containsKey(nb.val)) {
                    byVal.put(nb.val, nb);
                    queue.add(nb);
                }
            }
        }
        return byVal;
    }

    // Convert a graph back to an adjacency list (in the same format as the input).
    static List<List<Integer>> toAdjList(Node start) {
        List<List<Integer>> result = new ArrayList<>();
        for (Node n : collect(start).values()) {
            List<Integer> row = new ArrayList<>();
            for (Node nb : n.neighbors) row.add(nb.val);
            result.add(row);
        }
        return result;
    }

    // True if no node object in the clone is the same reference as one in the original.
    static boolean isDeepCopy(Node original, Node clone) {
        Set<Node> originals = Collections.newSetFromMap(new IdentityHashMap<>());
        originals.addAll(collect(original).values());
        for (Node n : collect(clone).values()) {
            if (originals.contains(n)) return false;
        }
        return true;
    }

    static int passed = 0, failed = 0;

    static void runTest(String name, int[][] adjList) {
        Node original = buildGraph(adjList);
        Node clone = new Solution().cloneGraph(original);   // fresh Solution per test

        List<List<Integer>> expected = toAdjList(original);
        List<List<Integer>> actual = toAdjList(clone);

        boolean sameStructure = expected.equals(actual);
        boolean nullHandled = (original == null) == (clone == null);
        boolean deep = (original == null) || isDeepCopy(original, clone);
        boolean ok = sameStructure && nullHandled && deep;

        System.out.printf("%-34s %s%n", name, ok ? "PASS" : "FAIL");
        System.out.println("  original: " + expected);
        System.out.println("  clone:    " + actual
                + (original != null ? "   (no shared nodes: " + deep + ")" : ""));
        if (ok) passed++; else failed++;
    }

    public static void main(String[] args) {
        // Example 1: 4-node cycle (square)
        runTest("Example 1: 4-cycle", new int[][] {{2, 4}, {1, 3}, {2, 4}, {1, 3}});

        // Example 2: single node, no neighbors
        runTest("Example 2: single node", new int[][] {{}});

        // Example 3: empty graph (null input)
        runTest("Example 3: empty graph", new int[][] {});

        // Two nodes connected to each other (smallest cycle in undirected terms)
        runTest("Two nodes", new int[][] {{2}, {1}});

        // Triangle: every node connected to every other
        runTest("Triangle (complete K3)", new int[][] {{2, 3}, {1, 3}, {1, 2}});

        // Star: node 1 is the hub
        runTest("Star (hub = 1)", new int[][] {{2, 3, 4, 5}, {1}, {1}, {1}, {1}});

        // Long chain 1-2-3-...-100 (max constraint; deepest recursion)
        int n = 100;
        int[][] chain = new int[n][];
        for (int i = 1; i <= n; i++) {
            if (i == 1) chain[i - 1] = new int[] {2};
            else if (i == n) chain[i - 1] = new int[] {n - 1};
            else chain[i - 1] = new int[] {i - 1, i + 1};
        }
        Node chainOriginal = buildGraph(chain);
        Node chainClone = new Solution().cloneGraph(chainOriginal);
        boolean chainOk = toAdjList(chainOriginal).equals(toAdjList(chainClone))
                && isDeepCopy(chainOriginal, chainClone);
        System.out.printf("%-34s %s%n", "Chain of 100 nodes", chainOk ? "PASS" : "FAIL");
        if (chainOk) passed++; else failed++;

        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }
}
