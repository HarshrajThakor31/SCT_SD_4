package com.skillcraft.sudoku.desktop;

public class SimpleSudokuSolver {
    
    public boolean solve(int[][] grid) {
        return backtrack(grid, 0, 0);
    }
    
    private boolean backtrack(int[][] grid, int row, int col) {
        if (row == 9) return true;
        if (col == 9) return backtrack(grid, row + 1, 0);
        if (grid[row][col] != 0) return backtrack(grid, row, col + 1);
        
        for (int num = 1; num <= 9; num++) {
            if (isValid(grid, row, col, num)) {
                grid[row][col] = num;
                if (backtrack(grid, row, col + 1)) {
                    return true;
                }
                grid[row][col] = 0;
            }
        }
        return false;
    }
    
    private boolean isValid(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num) return false;
        }
        
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (grid[i][j] == num) return false;
            }
        }
        return true;
    }
}