import java.util.Arrays;

public class PairSum {

    /* Given an array of integers sorted in ascending order and a target value,
    return the indexes of any pair of numbers in the array that sum to the target.
    The order of the indexes in the result doesn't matter. If no pair is found, return an empty array.
     */
    static int[] getPairSum(int[] input, int target) {
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
     * Runs getPairSum and validates the result.
     * Because the problem allows ANY valid pair in ANY order, this checks the result's
     * properties rather than exact index values:
     *   expectPair = true  -> exactly 2 distinct, in-bounds indexes whose values sum to target
     *   expectPair = false -> an empty array
     */
    static void check(String name, int[] input, int target, boolean expectPair) {
        int[] result = getPairSum(input, target);
        String error = null;

        if (!expectPair) {
            if (result.length != 0) {
                error = "expected [] but got " + Arrays.toString(result);
            }
        } else if (result.length != 2) {
            error = "expected a pair but got " + Arrays.toString(result);
        } else {
            int i = result[0], j = result[1];
            if (i < 0 || j < 0 || i >= input.length || j >= input.length) {
                error = "index out of bounds: " + Arrays.toString(result);
            } else if (i == j) {
                error = "same index used twice: " + Arrays.toString(result);
            } else if ((long) input[i] + input[j] != target) {
                error = "input[" + i + "] + input[" + j + "] = "
                        + ((long) input[i] + input[j]) + ", not " + target;
            }
        }

        if (error == null) {
            passed++;
            System.out.println("PASS " + name + ": " + Arrays.toString(input)
                    + ", target " + target + " -> " + Arrays.toString(result));
        } else {
            failed++;
            System.out.println("FAIL " + name + ": " + Arrays.toString(input)
                    + ", target " + target + " -> " + error);
        }
    }

    public static void main(String[] args) {
        check("basic",            new int[] {-5, -2, 3, 4, 6}, 7, true);
        check("duplicates",       new int[] {1, 1, 1}, 2, true);
        check("no pair",          new int[] {1, 2, 3}, 100, false);
        check("empty",            new int[] {}, 5, false);
        check("single element",   new int[] {5}, 10, false);
        check("overflow: false match",
                new int[] {Integer.MAX_VALUE - 1, Integer.MAX_VALUE}, -3, false);
        check("overflow: missed pair",
                new int[] {1, 3, Integer.MAX_VALUE}, 4, true);

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            throw new AssertionError(failed + " test(s) failed");
        }
    }
}
