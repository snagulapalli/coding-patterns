import java.util.Arrays;

public class ProductOfArrayExceptSelfUsingNoExtraSpace {
    // Time: O(n)
    // Space: O(1) extra (output array not counted)
    public int[] productExceptSelf(int[] nums) {
        int[] result = new int[nums.length];
        result[0] = 1;
        for (int i = 1; i < nums.length; i++) { //step1: result has left products
            result[i] = result[i - 1] * nums[i-1];
        }

        int rightProductValue = 1;
        for (int i = nums.length -1; i>=0;i--) {
            result[i] = result[i] * rightProductValue;
            rightProductValue *= nums[i];
        }
        return result;
    }

    public static void main(String[] args) {
        ProductOfArrayExceptSelfUsingNoExtraSpace sol = new ProductOfArrayExceptSelfUsingNoExtraSpace();

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
