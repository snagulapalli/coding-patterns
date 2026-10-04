// Number of Islands - recursive DFS ("sink the island") approach
//
// Time:  O(m*n) - each cell is scanned once and receives at most 4 dfs calls;
//                 once sunk to '0', those calls return in O(1)
// Space: O(m*n) worst case - recursion depth can reach m*n on an all-land grid
//                 (no extra visited array; the grid is mutated in place)
public class NumberOfIslandsUsingRecursion {

    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;

        int islandCount = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    dfs(grid, i, j);
                    islandCount++;
                }
            }
        }
        return islandCount;
    }

    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length ||
            j < 0 || j >= grid[0].length ||
            grid[i][j] == '0') return;

        grid[i][j] = '0'; // sink this cell so it's never counted again

        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }

    // ---------------- test harness ----------------

    private static int passed = 0;
    private static int failed = 0;

    // Builds a grid from strings like "11000" for readability
    private static char[][] grid(String... rows) {
        char[][] g = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
        return g;
    }

    private static void check(String name, char[][] grid, int expected) {
        // numIslands mutates the grid, so each test builds its own copy
        int actual = new NumberOfIslandsUsingRecursion().numIslands(grid);
        if (actual == expected) {
            passed++;
            System.out.printf("PASS  %-32s -> %d%n", name, actual);
        } else {
            failed++;
            System.out.printf("FAIL  %-32s -> expected %d, got %d%n", name, expected, actual);
        }
    }

    public static void main(String[] args) {
        // LeetCode examples
        check("Example 1 (one big island)", grid(
                "11110",
                "11010",
                "11000",
                "00000"), 1);

        check("Example 2 (three islands)", grid(
                "11000",
                "11000",
                "00100",
                "00011"), 3);

        check("Example 3 (single water cell)", grid("0"), 0);

        // Edge cases
        check("Single land cell", grid("1"), 1);
        check("Empty grid", new char[0][0], 0);
        check("Null grid", null, 0);
        check("All water", grid("000", "000"), 0);
        check("All land", grid("111", "111", "111"), 1);
        check("Single row, alternating", grid("10101"), 3);
        check("Single column, gaps", grid("1", "1", "0", "1"), 2);

        // Diagonal neighbors do NOT connect
        check("Diagonal cells are separate", grid(
                "100",
                "010",
                "001"), 3);

        check("Checkerboard 4x4", grid(
                "1010",
                "0101",
                "1010",
                "0101"), 8);

        // Shapes that need DFS to turn corners / backtrack
        check("U-shape", grid(
                "101",
                "101",
                "111"), 1);

        check("Ring with lake and inner island", grid(
                "11111",
                "10001",
                "10101",
                "10001",
                "11111"), 2);

        check("Spiral", grid(
                "11111",
                "00001",
                "11101",
                "10001",
                "11111"), 1);

        System.out.printf("%n%d passed, %d failed%n", passed, failed);
    }
}
