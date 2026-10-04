import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

//Time: O(n*m) every cell is visited once + checks for its potential four neighbors
//Space: O(n*m) Queue could in worst case be n*m if every cell is rotten
//CAVEAT: This solution modifies grid in place.
public class RottenOrangesUsingMultisourceBFS {

    public int orangesRotting(int[][] grid) {

        if (grid == null || grid.length == 0) return 0;
        int rows = grid.length;
        int cols = grid[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        int fresh = 0;
        int minutes = 0;

        //Put on queue all cells containing rotten oranges
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                } else if (grid[i][j] == 1) {
                    fresh++;
                }
            }
        }

        int[][] directions = {
            {0, -1}, //left
            {0, 1},  //right
            {1, 0},  //down
            {-1, 0}  //up
        };

        //Process one level or one minute at a time
        while (!queue.isEmpty() && fresh > 0) {
            int size = queue.size();

            //Every currently rotten one spoils its neighbors
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int r = current[0];
                int c = current[1];

                for (int[] direction : directions) {
                    int nr = r + direction[0];
                    int nc = c + direction[1];
                    //Check if this is a valid fresh one?
                    if (nr >= 0 && nr < rows &&
                        nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                        //make it rotten
                        grid[nr][nc] = 2;
                        fresh--; //decrement fresh ones as this now gotten rotten
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
            minutes++;
        }

        if (fresh > 0) return -1;
        return minutes;
    }

    // ---------------- Test harness ----------------

    private static int passed = 0;
    private static int failed = 0;

    private static void runTest(String name, int[][] grid, int expected) {
        String before = Arrays.deepToString(grid);
        // Pass a copy since the solution mutates the grid
        int actual = new RottenOrangesUsingMultisourceBFS().orangesRotting(deepCopy(grid));
        boolean ok = actual == expected;
        if (ok) passed++; else failed++;
        System.out.printf("%-4s %-38s grid=%s expected=%d actual=%d%n",
                ok ? "PASS" : "FAIL", name, before, expected, actual);
    }

    private static int[][] deepCopy(int[][] grid) {
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) copy[i] = grid[i].clone();
        return copy;
    }

    public static void main(String[] args) {
        // LeetCode examples
        runTest("Example 1", new int[][]{{2,1,1},{1,1,0},{0,1,1}}, 4);
        runTest("Example 2 (unreachable fresh)", new int[][]{{2,1,1},{0,1,1},{1,0,1}}, -1);
        runTest("Example 3 (no fresh)", new int[][]{{0,2}}, 0);

        // Edge cases
        runTest("Single empty cell", new int[][]{{0}}, 0);
        runTest("Single fresh, no rotten", new int[][]{{1}}, -1);
        runTest("Single rotten", new int[][]{{2}}, 0);
        runTest("All empty", new int[][]{{0,0},{0,0}}, 0);
        runTest("Fresh only, no rotten", new int[][]{{1,1},{1,1}}, -1);

        // Multi-source: two rotten spread from both ends
        runTest("Multi-source row", new int[][]{{2,1,1,1,1,2}}, 2);
        runTest("Multi-source corners", new int[][]{{2,1,1},{1,1,1},{1,1,2}}, 2);

        // Longest path in a snake-like layout
        runTest("Snake path", new int[][]{{2,1,1},{0,0,1},{1,1,1}}, 6);

        // Diagonal adjacency should NOT spread
        runTest("Diagonal only (no spread)", new int[][]{{2,0},{0,1}}, -1);

        System.out.printf("%nPassed: %d, Failed: %d%n", passed, failed);
    }
}
