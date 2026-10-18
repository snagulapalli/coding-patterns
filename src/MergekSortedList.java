import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Merge K Sorted Lists (LeetCode 23)
 *
 * Given an array of k sorted linked lists, merge them into one sorted list.
 *
 * Approach: min-heap of the current heads.
 *   - The next smallest node must be one of the k current heads.
 *   - Seed the heap with all non-null heads, then repeatedly poll the minimum,
 *     append it to the result, and push its successor (if any).
 *
 * Time:  O(N log k) - N = total nodes across all lists. Each node is offered
 *                     and polled once; the heap never holds more than k nodes,
 *                     so each heap operation costs O(log k).
 * Space: O(k)       - the heap. Output reuses existing nodes (relinking only).
 *
 * Follow-up (no heap): divide and conquer, pairwise merging like merge sort.
 *   log k rounds x O(N) work per round = O(N log k) time, O(1) extra space
 *   if done iteratively.
 *
 * Practice: delete the body of mergeKLists() and rewrite it, then run:
 *   javac MergekSortedList.java && java MergekSortedList
 */
public class MergekSortedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // ======================= PRACTICE: REWRITE THIS METHOD =======================
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null) return null;

        PriorityQueue<ListNode> pq =
                new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));
        for (ListNode head : lists) {
            if (head != null) pq.offer(head);       // seed with k heads (skip nulls)
        }

        ListNode dummy = new ListNode(0), tail = dummy;
        while (!pq.isEmpty()) {
            ListNode node = pq.poll();              // smallest current head
            tail.next = node;
            tail = node;
            if (node.next != null) pq.offer(node.next);  // successor replaces it
        }
        return dummy.next;
    }
    // =============================================================================

    // ---------------------------- Test helpers ----------------------------

    static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0), tail = dummy;
        for (int v : vals) { tail.next = new ListNode(v); tail = tail.next; }
        return dummy.next;
    }

    static List<Integer> toList(ListNode head) {
        List<Integer> out = new ArrayList<>();
        for (ListNode n = head; n != null; n = n.next) out.add(n.val);
        return out;
    }

    static int passed = 0, failed = 0;

    static void check(String name, ListNode[] input, Integer... expected) {
        List<Integer> actual;
        try {
            actual = toList(mergeKLists(input));
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL  " + name + "  -> threw " + e);
            return;
        }
        List<Integer> exp = Arrays.asList(expected);
        if (actual.equals(exp)) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name + "  expected " + exp + " but got " + actual);
        }
    }

    public static void main(String[] args) {
        check("classic example",
                new ListNode[]{ build(1, 4, 5), build(1, 3, 4), build(2, 6) },
                1, 1, 2, 3, 4, 4, 5, 6);

        check("empty array",
                new ListNode[]{});

        check("all lists null",
                new ListNode[]{ null, null });

        check("mix of null and non-null",
                new ListNode[]{ null, build(2), null, build(1, 3) },
                1, 2, 3);

        check("single list",
                new ListNode[]{ build(1, 2, 3) },
                1, 2, 3);

        check("duplicates across lists",
                new ListNode[]{ build(2, 2), build(2), build(2, 2, 2) },
                2, 2, 2, 2, 2, 2);

        check("negatives",
                new ListNode[]{ build(-5, -1, 0), build(-3, 2) },
                -5, -3, -1, 0, 2);

        check("extreme values (catches a.val - b.val overflow)",
                new ListNode[]{ build(Integer.MIN_VALUE, 0), build(Integer.MAX_VALUE) },
                Integer.MIN_VALUE, 0, Integer.MAX_VALUE);

        check("uneven lengths",
                new ListNode[]{ build(10), build(1, 2, 3, 4, 5, 6, 7, 8, 9) },
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
