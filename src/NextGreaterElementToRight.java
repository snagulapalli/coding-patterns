import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Next Greater Element to the Right.
 * For each element, find the first element to its right that is strictly greater.
 * If none exists, the answer is -1.
 *
 * Time:  O(n) - each element is pushed and popped at most once
 * Space: O(n) - stack + result array
 */
public class NextGreaterElementToRight {

    public int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {
            // Remove elements that cannot be the answer
            while (!stack.isEmpty() && stack.peek() <= nums[i]) {
                stack.pop();
            }
            // The first larger element to the right
            if (!stack.isEmpty()) {
                result[i] = stack.peek();
            }
            // Current element may be the next greater
            // for an element to its left
            stack.push(nums[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        NextGreaterElementToRight solver = new NextGreaterElementToRight();

        int[][] inputs = {
            {4, 5, 2, 25},          // mixed
            {13, 7, 6, 12},         // dips then rise
            {1, 2, 3, 4, 5},        // strictly increasing
            {5, 4, 3, 2, 1},        // strictly decreasing
            {2, 2, 2},              // all equal (strictly greater -> all -1)
            {1, 3, 2, 4},           // classic example
            {6, 8, 0, 1, 3},        // GfG example
            {42},                   // single element
            {}                      // empty
        };

        int[][] expected = {
            {5, 25, 25, -1},
            {-1, 12, 12, -1},
            {2, 3, 4, 5, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1},
            {3, 4, 4, -1},
            {8, -1, 1, 3, -1},
            {-1},
            {}
        };

        int passed = 0;
        for (int t = 0; t < inputs.length; t++) {
            int[] actual = solver.nextGreaterElement(inputs[t]);
            boolean ok = Arrays.equals(actual, expected[t]);
            if (ok) passed++;
            System.out.printf("Test %d: input=%s -> output=%s  %s%n",
                    t + 1,
                    Arrays.toString(inputs[t]),
                    Arrays.toString(actual),
                    ok ? "PASS" : "FAIL (expected " + Arrays.toString(expected[t]) + ")");
        }
        System.out.printf("%n%d/%d tests passed%n", passed, inputs.length);
    }
}
