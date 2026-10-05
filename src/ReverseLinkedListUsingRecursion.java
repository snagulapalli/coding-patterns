/**
 * Reverse a singly linked list recursively.
 *
 * Time:  O(n) - every node is visited once
 * Space: O(n) - recursive call stack
 */
public class ReverseLinkedListUsingRecursion {

    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    // ---------- Test helpers ----------

    private static ListNode buildList(int[] values) {
        ListNode dummy = new ListNode();
        ListNode tail = dummy;
        for (int v : values) {
            tail.next = new ListNode(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    private static String toString(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(" -> ");
            head = head.next;
        }
        return sb.append("]").toString();
    }

    private static void runTest(String name, int[] input, int[] expected) {
        ReverseLinkedListUsingRecursion solver = new ReverseLinkedListUsingRecursion();
        ListNode head = buildList(input);
        String before = toString(head);
        String actual = toString(solver.reverseList(head));
        String want = toString(buildList(expected));
        String status = actual.equals(want) ? "PASS" : "FAIL";
        System.out.printf("%-4s %-14s input=%-24s output=%s%n", status, name, before, actual);
    }

    public static void main(String[] args) {
        runTest("empty",        new int[]{},              new int[]{});
        runTest("single",       new int[]{1},             new int[]{1});
        runTest("two nodes",    new int[]{1, 2},          new int[]{2, 1});
        runTest("five nodes",   new int[]{1, 2, 3, 4, 5}, new int[]{5, 4, 3, 2, 1});
        runTest("duplicates",   new int[]{7, 7, 3, 7},    new int[]{7, 3, 7, 7});
        runTest("negatives",    new int[]{-1, 0, 1},      new int[]{1, 0, -1});
    }
}
