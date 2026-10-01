import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

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

    private static int passed = 0;
    private static int failed = 0;

    /**
     * Runs the given pair-sum method and validates the result.
     * Because the problem allows ANY valid pair in ANY order, this checks the result's
     * properties rather than exact index values:
     *   expectPair = true  -> exactly 2 distinct, in-bounds indexes whose values sum to target
     *   expectPair = false -> an empty array
     * Validation runs against a copy of the ORIGINAL input, and the method must not
     * modify the caller's array.
     */
    static void check(String name, BiFunction<int[], Integer, int[]> method,
                      int[] input, int target, boolean expectPair) {
        int[] original = input.clone();
        int[] result = method.apply(input, target);
        String error = null;

        if (!Arrays.equals(original, input)) {
            error = "input was modified: " + Arrays.toString(original)
                    + " became " + Arrays.toString(input);
        } else if (!expectPair) {
            if (result.length != 0) {
                error = "expected [] but got " + Arrays.toString(result);
            }
        } else if (result.length != 2) {
            error = "expected a pair but got " + Arrays.toString(result);
        } else {
            int i = result[0], j = result[1];
            if (i < 0 || j < 0 || i >= original.length || j >= original.length) {
                error = "index out of bounds: " + Arrays.toString(result);
            } else if (i == j) {
                error = "same index used twice: " + Arrays.toString(result);
            } else if ((long) original[i] + original[j] != target) {
                error = "input[" + i + "] + input[" + j + "] = "
                        + ((long) original[i] + original[j]) + ", not " + target;
            }
        }

        if (error == null) {
            passed++;
            System.out.println("PASS " + name + ": " + Arrays.toString(original)
                    + ", target " + target + " -> " + Arrays.toString(result));
        } else {
            failed++;
            System.out.println("FAIL " + name + ": " + Arrays.toString(original)
                    + ", target " + target + " -> " + error);
        }
    }

    public static void main(String[] args) {
        BiFunction<int[], Integer, int[]> sorted = PairSum::getPairSumSorted;
        BiFunction<int[], Integer, int[]> unsorted = PairSum::getPairSumUnsortedUsingHashMap;

        System.out.println("=== getPairSumSorted ===");
        check("basic",                    sorted, new int[] {-5, -2, 3, 4, 6}, 7, true);
        check("duplicates",               sorted, new int[] {1, 1, 1}, 2, true);
        check("no pair",                  sorted, new int[] {1, 2, 3}, 100, false);
        check("empty",                    sorted, new int[] {}, 0, false);
        check("single element",           sorted, new int[] {5}, 10, false);
        check("single element (no self)", sorted, new int[] {5}, 10, false);
        check("pair happy path",          sorted, new int[] {2, 3}, 5, true);
        check("pair unhappy path",        sorted, new int[] {2, 4}, 5, false);
        check("with negative values",     sorted, new int[] {-1, 2, 3}, 2, true);
        check("all negative values",      sorted, new int[] {-3, -2, -1}, -5, true);
        check("overflow: false match",    sorted,
                new int[] {Integer.MAX_VALUE - 1, Integer.MAX_VALUE}, -3, false);
        check("overflow: missed pair",    sorted,
                new int[] {1, 3, Integer.MAX_VALUE}, 4, true);

        System.out.println("\n=== getPairSumUnsorted ===");
        check("basic (shuffled)",         unsorted, new int[] {6, -2, 4, -5, 3}, 7, true);
        check("pair at far ends",         unsorted, new int[] {9, 0, 5, 7, 1}, 10, true);
        check("descending order",         unsorted, new int[] {10, 8, 6, 4, 2}, 6, true);
        check("duplicates",               unsorted, new int[] {3, 1, 3}, 6, true);
        check("no self-pairing",          unsorted, new int[] {4, 1, 2}, 8, false);
        check("no pair",                  unsorted, new int[] {3, 1, 2}, 100, false);
        check("empty",                    unsorted, new int[] {}, 0, false);
        check("single element",           unsorted, new int[] {5}, 10, false);
        check("pair happy path",          unsorted, new int[] {3, 2}, 5, true);
        check("pair unhappy path",        unsorted, new int[] {4, 2}, 5, false);
        check("with negative values",     unsorted, new int[] {3, -1, 2}, 2, true);
        check("all negative values",      unsorted, new int[] {-1, -3, -2}, -5, true);
        check("zeros",                    unsorted, new int[] {0, 5, 0}, 0, true);
        check("overflow: false match",    unsorted,
                new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE - 1}, -3, false);
        check("overflow: missed pair",    unsorted,
                new int[] {Integer.MAX_VALUE, 3, 1}, 4, true);
        check("overflow: complement out of int range", unsorted,
                new int[] {Integer.MIN_VALUE, 5}, Integer.MAX_VALUE, false);
        check("overflow: wrapped complement collides", unsorted,
                new int[] {Integer.MIN_VALUE, -1}, Integer.MAX_VALUE, false);
        check("dup value, no self-pair", unsorted, new int[] {3, 4, 3}, 6, true);

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            throw new AssertionError(failed + " test(s) failed");
        }
    }
}