
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

public class TripletSum {

    record Triplet(int a, int b, int c) {
    }

    /* Given an array of integers return all triplets [a,b,c] such that a+b+c=0.
    The solution must not contain duplicates. If no such triplets are found, return an empty array.
    Each triplet can be arranged in any order, and the output can be returned in any order.

    Approach: sort a COPY, fix each distinct value a = nums[i], and call getPairSumSorted
    (unchanged apart from a long target) on the sorted sub-array to the right of i, with target -a.
    It returns only ONE pair, so after a hit at (l, r) we search again inside sub[l+1 .. r-1].
    That's safe because of the two-pointer invariant: when the scan stops at (l, r), every pair
    using an element outside [l, r] has already been ruled out, and any other pair using l or r
    would repeat the same values. Returned indexes are relative to the sub-array passed in.

    Cost: each call gets a fresh copy (Arrays.copyOfRange) and restarts its scan, so this is
    O(n^2) typically but O(n^3) worst case (many pairs per anchor), vs O(n^2) for an in-place
    range scan. Space O(n). The caller's array is not modified.
    Triplets are built from the sorted array, so each is stored as (a <= b <= c), which makes
    record equality a reliable de-duplication key.
     */
    public Set<Triplet> getTripletSum(int[] input) {
        Set<Triplet> result = new HashSet<>();
        if (input == null || input.length < 3) {
            return result;
        }

        int[] nums = input.clone();
        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            if (nums[i] > 0) {
                break;                                  // smallest remaining value > 0: no sum can reach 0
            }
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;                               // same 'a' as before: would only repeat triplets
            }

            long target = -(long) nums[i];              // long: -Integer.MIN_VALUE overflows an int
            int[] sub = Arrays.copyOfRange(nums, i + 1, n);
            while (true) {
                int[] pair = getPairSumSorted(sub, target);
                if (pair.length == 0) {
                    break;
                }
                int l = pair[0];
                int r = pair[1];
                result.add(new Triplet(nums[i], sub[l], sub[r]));
                sub = Arrays.copyOfRange(sub, l + 1, r);   // remaining candidates: strictly inside (l, r)
            }
        }
        return result;
    }

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

    /* Given an array of integers sorted in ascending order and a target value,
    return the indexes of any pair of numbers in the array that sum to the target.
    The order of the indexes in the result doesn't matter. If no pair is found, return an empty array.
     */
    static int[] getPairSumSorted(int[] input, long target) {   // long (was int): lets getTripletSum pass -Integer.MIN_VALUE
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

        report(name, Arrays.toString(original) + ", target " + target,
                error, Arrays.toString(result));
    }

    /**
     * Brute-force O(n^3) reference: every distinct value-triplet (stored sorted) summing to 0.
     * Uses long arithmetic so int overflow can't produce false matches.
     */
    static Set<Triplet> bruteForceTriplets(int[] input) {
        Set<Triplet> expected = new HashSet<>();
        for (int i = 0; i < input.length; i++) {
            for (int j = i + 1; j < input.length; j++) {
                for (int k = j + 1; k < input.length; k++) {
                    if ((long) input[i] + input[j] + input[k] == 0) {
                        int[] t = {input[i], input[j], input[k]};
                        Arrays.sort(t);
                        expected.add(new Triplet(t[0], t[1], t[2]));
                    }
                }
            }
        }
        return expected;
    }

    /**
     * Runs getTripletSum and compares it to an expected set (as a set: order doesn't matter).
     * Also checks that the caller's array was not modified.
     */
    static void checkTriplets(String name, int[] input, Set<Triplet> expected) {
        int[] original = input.clone();
        Set<Triplet> result = new TripletSum().getTripletSum(input);
        String error = null;

        if (!Arrays.equals(original, input)) {
            error = "input was modified: " + Arrays.toString(original)
                    + " became " + Arrays.toString(input);
        } else if (!result.equals(expected)) {
            error = "expected " + expected + " but got " + result;
        }

        report(name, Arrays.toString(original), error, result.toString());
    }

    static Set<Triplet> triplets(int[]... values) {
        Set<Triplet> set = new HashSet<>();
        for (int[] v : values) {
            set.add(new Triplet(v[0], v[1], v[2]));
        }
        return set;
    }

    static void report(String name, String inputDesc, String error, String resultDesc) {
        if (error == null) {
            passed++;
            System.out.println("PASS " + name + ": " + inputDesc + " -> " + resultDesc);
        } else {
            failed++;
            System.out.println("FAIL " + name + ": " + inputDesc + " -> " + error);
        }
    }

    public static void main(String[] args) {
        BiFunction<int[], Integer, int[]> sorted = TripletSum::getPairSumSorted;
        BiFunction<int[], Integer, int[]> unsorted = TripletSum::getPairSumUnsorted;

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

        System.out.println("\n=== getTripletSum ===");
        checkTriplets("classic",            new int[] {-1, 0, 1, 2, -1, -4},
                triplets(new int[] {-1, -1, 2}, new int[] {-1, 0, 1}));
        checkTriplets("all zeros",          new int[] {0, 0, 0, 0},
                triplets(new int[] {0, 0, 0}));
        checkTriplets("no triplet",         new int[] {0, 1, 1}, triplets());
        checkTriplets("all positive",       new int[] {1, 2, 3, 4}, triplets());
        checkTriplets("all negative",       new int[] {-4, -3, -2, -1}, triplets());
        checkTriplets("empty",              new int[] {}, triplets());
        checkTriplets("one element",        new int[] {0}, triplets());
        checkTriplets("two elements",       new int[] {0, 0}, triplets());
        checkTriplets("exactly three",      new int[] {3, -1, -2},
                triplets(new int[] {-2, -1, 3}));
        checkTriplets("heavy duplicates",   new int[] {-2, 0, 0, 2, 2, -2, 0, 1, 1},
                triplets(new int[] {-2, 0, 2}, new int[] {-2, 1, 1}, new int[] {0, 0, 0}));
        checkTriplets("several per anchor", new int[] {-4, -1, -1, 0, 1, 2, 2, 3, 4},
                bruteForceTriplets(new int[] {-4, -1, -1, 0, 1, 2, 2, 3, 4}));
        checkTriplets("overflow: MIN + 1 + MAX = 0",
                new int[] {Integer.MAX_VALUE, 1, Integer.MIN_VALUE},
                triplets(new int[] {Integer.MIN_VALUE, 1, Integer.MAX_VALUE}));
        checkTriplets("overflow: int sum wraps to 0, real sum doesn't",
                new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE, 0}, triplets());
        checkTriplets("overflow: MAX + MAX + 2 wraps to 0",
                new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, 2}, triplets());

        // Randomized cross-check against the brute-force reference (fixed seed: reproducible).
        Random random = new Random(42);
        int fuzzFailuresBefore = failed;
        for (int run = 0; run < 2000; run++) {
            int[] input = new int[random.nextInt(13)];
            for (int k = 0; k < input.length; k++) {
                input[k] = random.nextInt(11) - 5;      // values in [-5, 5]: lots of duplicates
            }
            Set<Triplet> expected = bruteForceTriplets(input);
            Set<Triplet> actual = new TripletSum().getTripletSum(input.clone());
            if (!actual.equals(expected)) {
                report("fuzz #" + run, Arrays.toString(input),
                        "expected " + expected + " but got " + actual, null);
            }
        }
        if (failed == fuzzFailuresBefore) {
            report("fuzz: 2000 random arrays vs brute force", "len 0-12, values -5..5",
                    null, "all matched");
        }

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            throw new AssertionError(failed + " test(s) failed");
        }
    }
}