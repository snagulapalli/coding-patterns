import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 560 - Subarray Sum Equals K (Medium)
 *
 * Given an array of integers nums and an integer k, return the total number
 * of subarrays whose sum equals k. A subarray is a contiguous non-empty
 * sequence of elements within an array.
 *
 * Approach: prefix sum + hashmap of (prefixSum -> how many times seen).
 * A subarray (i+1..j) sums to k  <=>  prefix[j] - prefix[i] == k
 *                                <=>  prefix[i] == prefix[j] - k  ("need").
 * So at each j, add the count of earlier prefixes equal to need.
 *
 * Time:  O(n)
 * Space: O(n)
 */
public class SubarraySumEqualsK {

    // NOTE: Sliding window works only if all elements are strictly positive.
    //       Prefix sum + hashmap handles negatives and zeros.
    // Caveat: currentPrefixSum/need can overflow int for large inputs;
    //         use long and Map<Long, Integer> if constraints aren't bounded.
    // Order matters: look up need BEFORE recording the current prefix,
    //         otherwise k == 0 would count the empty subarray.
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> sumCountMap = new HashMap<>();

        sumCountMap.put(0, 1); // empty prefix: lets subarrays starting at index 0 count

        int result = 0;
        int currentPrefixSum = 0;

        for (int num : nums) {
            currentPrefixSum += num;
            int need = currentPrefixSum - k;
            result += sumCountMap.getOrDefault(need, 0);
            sumCountMap.merge(currentPrefixSum, 1, Integer::sum);
        }
        return result;
    }

    public static void main(String[] args) {
        SubarraySumEqualsK s = new SubarraySumEqualsK();

        // { nums, k, expected }
        Object[][] cases = {
            { new int[]{1, 1, 1}, 2, 2 },                     // LeetCode example 1
            { new int[]{1, 2, 3}, 3, 2 },                     // LeetCode example 2: [1,2], [3]
            { new int[]{5}, 5, 1 },                           // single element match
            { new int[]{1}, 0, 0 },                           // single element, no match
            { new int[]{1, -1, 0}, 0, 3 },                    // negatives: [1,-1], [0], [1,-1,0]
            { new int[]{-1, -1, 1}, 0, 1 },                   // negatives: [-1,1]
            { new int[]{0, 0, 0}, 0, 6 },                     // zeros: why sliding window fails
            { new int[]{3, 4, 7, 2, -3, 1, 4, 2}, 7, 4 },     // mixed: [3,4], [7], [7,2,-3,1], [1,4,2]
        };

        int passed = 0;
        for (Object[] c : cases) {
            int[] nums = (int[]) c[0];
            int k = (int) c[1];
            int expected = (int) c[2];
            int actual = s.subarraySum(nums, k);
            boolean ok = actual == expected;
            if (ok) passed++;
            System.out.printf("%s nums=%s k=%d -> %d (expected %d)%n",
                    ok ? "PASS" : "FAIL", java.util.Arrays.toString(nums), k, actual, expected);
        }
        System.out.printf("%n%d/%d passed%n", passed, cases.length);
    }
}
