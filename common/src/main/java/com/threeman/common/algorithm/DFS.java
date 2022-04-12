package com.threeman.common.algorithm;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/4/12 14:58
 * @描述 深度优先遍历
 */
public class DFS {

    /**
     * 根据深度优先算法的特性，可以使用栈先入后出的特性实现。
     * 将探索过的点存入栈内，遇到走不通的时候将栈顶元素出栈回到上一个元素，实现回溯
     */

    public static void  dfs(char[][] grid, int r, int c) {
        int nr = grid.length;
        int nc = grid[0].length;

        if (r < 0 || c < 0 || r >= nr || c >= nc || grid[r][c] == '0') {
            return;
        }

        grid[r][c] = '0';
        dfs(grid, r - 1, c);
        dfs(grid, r + 1, c);
        dfs(grid, r, c - 1);
        dfs(grid, r, c + 1);
    }

    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    ++num_islands;
                    dfs(grid, r, c);
                }
            }
        }

        return num_islands;
    }

}
