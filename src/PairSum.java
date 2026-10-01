import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PairSum {
    /* Given an array of integers not-sorted and a target value,
    return the indexes of any pair of numbers in the array that sum to the target.
    The order of the indexes in the result doesn't matter. If no pair is found, return an empty array.

    Indexes must refer to the ORIGINAL array, so we can't sort in place and reuse
    getPairSumSorted (that returns positions in the sorted order and mutates the caller's array).
    One pass with a value -> index map instead: O(n) time, O(n) space, input untouched.
     */
    static int[] getPairSumUnsorted(int[] input, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            long complement = (long) target - input[i];   // long: avoid overflow
            if (complement >= Integer.MIN_VALUE && complement <= Integer.MAX_VALUE) {
                Integer j = seen.get((int) complement);
                if (j != null) {
                    return new int[] {j, i};
                }
            }
            seen.putIfAbsent(input[i], i);
        }
        return new int[] {};
    }

    static int[] getPairSumUnsortedUsingHashMap(int[] input, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            map.put(input[i], i);
        }

        for (int i = 0; i < input.length; i++) {
            long complement = (long) target - input[i];
            if (complement < Integer.MIN_VALUE || complement > Integer.MAX_VALUE) {
                continue;                                   // can't be in an int array
            }
            Integer j = map.get((int) complement);          // Integer key, not Long
            if (j != null && j != i) {                      // don't pair an element with itself
                return new int[] {j, i};
            }
        }
        return new int[] {};
    }

    /* Given an array of integers sorted in ascending order and a target value,
    return the indexes of any pair of numbers in the array that sum to the target.
    The order of the indexes in the result doesn't matter. If no pair is found, return an empty array.
     */
    static int[] getPairSumSorted(int[] input, int target) {
        int leftIndex = 0;
        int rightIndex = input.length - 1;
        while (leftIndex < rightIndex) {
            long sum = (long) input[leftIndex] + input[rightIndex];
            if (sum == target) {
                return new int[] {leftIndex, rightIndex};
            } else if (sum < target) {
                leftIndex++;
            } else {
                rightIndex--;
            }
        }
        return new int[] {};
    }

    static boolean isValid(int[] a, int target, boolean expectPair, int[] r) {
        if (!expectPair)        return r.length == 0;   // no pair expected: must be empty
        if (r.length != 2)      return false;           // must be exactly two indexes
        if (r[0] == r[1])       return false;           // must be different elements
        return a[r[0]] + a[r[1]] == target;             // must actually sum to target
    }

    static void check(int[] a, int target, boolean expectPair) {
        int[] r = getPairSumUnsorted(a.clone(), target);
        boolean ok = isValid(a, target, expectPair, r);
        System.out.println((ok ? "ok   " : "FAIL ") + Arrays.toString(a) + " t=" + target + " -> " + Arrays.toString(r));
    }

    static void checkSorted(int[] a, int target, boolean expectPair) {
        int[] r = getPairSumSorted(a.clone(), target);
        boolean ok = isValid(a, target, expectPair, r);
        System.out.println((ok ? "ok   " : "FAIL ") + Arrays.toString(a) + " t=" + target + " -> " + Arrays.toString(r));
    }

    public static void main(String[] args) {
        System.out.println("=== getPairSumSorted ===");
        checkSorted( new int[] {-5, -2, 3, 4, 6}, 7, true);
        checkSorted(new int[] {1, 1, 1}, 2, true);
        checkSorted(new int[] {1, 2, 3}, 100, false);
        checkSorted(new int[] {}, 0, false);
        checkSorted(new int[] {5}, 10, false);
        checkSorted(new int[] {2, 3}, 5, true);
        checkSorted(new int[] {2, 4}, 5, false);
        checkSorted( new int[] {-1, 2, 3}, 2, true);
        checkSorted( new int[] {-3, -2, -1}, -5, true);
        checkSorted(new int[] {Integer.MAX_VALUE - 1, Integer.MAX_VALUE}, -3, false);
        checkSorted(new int[] {1, 3, Integer.MAX_VALUE}, 4, true);

        System.out.println("\n=== getPairSumUnsorted ===");
        check(new int[] {6, -2, 4, -5, 3}, 7, true);
        check(new int[] {9, 0, 5, 7, 1}, 10, true);
        check( new int[] {10, 8, 6, 4, 2}, 6, true);
        check(new int[] {3, 1, 3}, 6, true);
        check(new int[] {4, 1, 2}, 8, false);
        check(new int[] {3, 1, 2}, 100, false);
        check(new int[] {}, 0, false);
        check( new int[] {5}, 10, false);
        check( new int[] {3, 2}, 5, true);
        check(new int[] {4, 2}, 5, false);
        check( new int[] {3, -1, 2}, 2, true);
        check( new int[] {-1, -3, -2}, -5, true);
        check( new int[] {0, 5, 0}, 0, true);
        check(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE - 1}, -3, false);
        check(new int[] {Integer.MAX_VALUE, 3, 1}, 4, true);
        check(new int[] {Integer.MIN_VALUE, 5}, Integer.MAX_VALUE, false);
        check(new int[] {Integer.MIN_VALUE, -1}, Integer.MAX_VALUE, false);
        check(new int[] {3, 4, 3}, 6, true);
    }
}