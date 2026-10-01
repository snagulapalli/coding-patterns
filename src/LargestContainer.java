import java.util.Arrays;
import java.util.Random;

public class LargestContainer {

    /* Given an array of numbers, each representing the height of a vertical line on a graph.
    A container can be formed with any pair of these lines, along with the x-axis of the graph.
    Return the amount of water which the largest container can hold.
    Example:
    Input: heights = [2,7,8,3,7,6]
    Output: 24
     */
    static int getLargestContainer(int[] heights) {
        int max = 0;
        if (heights == null || heights.length <= 1) {return max;}

        int leftIndex = 0;
        int rightIndex = heights.length - 1;
        while (leftIndex < rightIndex) {
            int area = Math.min(heights[leftIndex], heights[rightIndex]) * (rightIndex - leftIndex) ;
            max = Math.max(area, max);
            if (heights[leftIndex] <= heights[rightIndex]) {
                leftIndex++;
            }else {rightIndex--;}
        }
        return max;
    }

    static int brute(int[] h) {                       // obviously correct, O(n²)
        int max = 0;
        for (int i = 0; i < h.length; i++)
            for (int j = i + 1; j < h.length; j++)
                max = Math.max(max, Math.min(h[i], h[j]) * (j - i));
        return max;
    }

    public static void main(String[] args) {
        int [] heights = {2,7,8,3,7,6};
        System.out.println("input: " + Arrays.toString(heights));
        System.out.println(getLargestContainer(heights));

        Random r = new Random(1);
        for (int t = 0; t < 1000; t++) {
            int[] a = new int[r.nextInt(10)];             // small sizes: 0..9
            for (int k = 0; k < a.length; k++) a[k] = r.nextInt(10);   // small values
            int got = getLargestContainer(a.clone());
            int want = brute(a.clone());
            if (got != want) {
                System.out.println(Arrays.toString(a) + " got " + got + " want " + want);
                break;                                    // first failure is enough
            }
        }
    }
}