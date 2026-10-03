import java.util.Arrays;

public class ProductOfArrayExceptSelfUsingExtraSpace {
    // Time: O(n)
    // Space: O(n)
    public int[] productExceptSelf(int[] nums) {

        int[] leftProducts = new int[nums.length];
        int[] rightProducts = new int[nums.length];

        int[] result = new int[nums.length];

        leftProducts[0] = 1;
        rightProducts[nums.length - 1] = 1;

        for (int i = 1; i < nums.length; i++) {
            leftProducts[i] = leftProducts[i - 1] * nums[i - 1];
        }

        for (int i = nums.length - 2; i >= 0; i--) {
            rightProducts[i] = rightProducts[i + 1] * nums[i + 1];
        }

        for (int i = 0; i < nums.length; i++) {
            result[i] = leftProducts[i] * rightProducts[i];
        }

        return result;
    }

    public static void main(String[] args) {
        ProductOfArrayExceptSelfUsingExtraSpace sol = new ProductOfArrayExceptSelfUsingExtraSpace();

        int[][] inputs = {
            {1, 2, 3, 4},        // Example 1
            {-1, 1, 0, -3, 3},   // Example 2
            {2, 3},              // minimum length
            {0, 0, 5},           // two zeros -> all zeros
            {-2, -3, 4}          // negatives
        };

        int[][] expected = {
            {24, 12, 8, 6},
            {0, 0, 9, 0, 0},
            {3, 2},
            {0, 0, 0},
            {-12, -8, 6}
        };

        for (int t = 0; t < inputs.length; t++) {
            int[] output = sol.productExceptSelf(inputs[t]);
            boolean pass = Arrays.equals(output, expected[t]);
            System.out.println("Input:    " + Arrays.toString(inputs[t]));
            System.out.println("Output:   " + Arrays.toString(output));
            System.out.println("Expected: " + Arrays.toString(expected[t]));
            System.out.println(pass ? "PASS" : "FAIL");
            System.out.println();
        }
    }
}
