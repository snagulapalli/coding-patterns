
public class PrefixSum {
    //Construction: O(n)
    //Time for sumRange: O(1)
    //Extra Space for prefix: O(n)

    private final int[] prefixSum;

    public PrefixSum(int[] nums) {
        this.prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
    }

    public int sumRange(int left, int right) {
        return this.prefixSum[right + 1] - this.prefixSum[left];
    }

    private static int passed = 0, failed = 0;

    private static void check(String label, int actual, int expected) {
        if (actual == expected) {
            passed++;
            System.out.printf("PASS  %-40s -> %d%n", label, actual);
        } else {
            failed++;
            System.out.printf("FAIL  %-40s -> got %d, expected %d%n", label, actual, expected);
        }
    }

    public static void main(String[] args) {
        // 1. LeetCode example
        PrefixSum ps = new PrefixSum(new int[]{-2, 0, 3, -5, 2, -1});
        check("LC example sumRange(0, 2)", ps.sumRange(0, 2), 1);
        check("LC example sumRange(2, 5)", ps.sumRange(2, 5), -1);
        check("LC example sumRange(0, 5)", ps.sumRange(0, 5), -3);

        // 2. left == 0 (the edge case the n+1 array fixes)
        PrefixSum ps2 = new PrefixSum(new int[]{3, 1, 4, 2});
        check("left == 0: sumRange(0, 2)", ps2.sumRange(0, 2), 8);

        // 3. Middle range
        check("middle: sumRange(1, 2)", ps2.sumRange(1, 2), 5);

        // 4. left == right (single element)
        check("left == right: sumRange(3, 3)", ps2.sumRange(3, 3), 2);

        // 5. Whole array
        check("whole array: sumRange(0, 3)", ps2.sumRange(0, 3), 10);

        // 6. Single-element array
        PrefixSum ps3 = new PrefixSum(new int[]{7});
        check("single-element array: sumRange(0, 0)", ps3.sumRange(0, 0), 7);

        // 7. All negatives
        PrefixSum ps4 = new PrefixSum(new int[]{-1, -2, -3});
        check("all negatives: sumRange(0, 2)", ps4.sumRange(0, 2), -6);

        // 8. Near LeetCode limits: 10^4 elements of 10^5 -> 10^9, fits in int
        int[] big = new int[10_000];
        java.util.Arrays.fill(big, 100_000);
        PrefixSum ps5 = new PrefixSum(big);
        check("max constraints: sumRange(0, 9999)", ps5.sumRange(0, 9999), 1_000_000_000);

        System.out.printf("%n%d passed, %d failed%n", passed, failed);
    }
}