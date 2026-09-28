import java.util.Arrays;

public class PairSum {

    /* Given an array of integers sorted in ascending order and a target value,
    return the indexes of any pair of numbers in the array that sum to the target.
    The order of the indexes in the result doesn't matter. If no pair is found, return an empty array.

     */

     static int[] getPairSum(int[] input, int target) {
        int[] returnPair = new int[2];

        int left_index = 0;
        int right_index =  input.length-1;
        while (left_index < right_index) {
            int sum = input[left_index] + input[right_index];
            if (sum == target) {
                return new int[] {left_index, right_index};
            }
            else if (sum < target) {
                left_index++;
            }else {
                right_index--;
            }
        }

        return returnPair;
    }

    public static void main(String[] args){
        System.out.println("This is Pair Sum problem");
        int [] input = new int[] {-5, -2, 3, 4,6};
        int target = 7;
        System.out.println("input:" + Arrays.toString(input) + "; target:" + target);
        int [] gotPair =  getPairSum(input, target);
        System.out.println("Got pair:" + Arrays.toString(gotPair));

        int [] input2 = new int[] {1,1,1};
        int target2 = 2;
        System.out.println("input2:" + Arrays.toString(input2) + "; target2:" + target2);
        int [] gotPair2 =  getPairSum(input2, target2);
        System.out.println("Got pair2:" + Arrays.toString(gotPair2));
    }

}
