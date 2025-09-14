package com.skillcraft.sudoku.desktop;

import java.util.*;

public class SimpleSudokuGenerator {
    private final Random random = new Random();
    
    public String generatePuzzle(String difficulty) {
        int[][] grid = generateCompleteGrid();
        int cellsToRemove = getCellsToRemove(difficulty);
        removeCells(grid, cellsToRemove);
        return gridToString(grid);
    }
    
    private int[][] generateCompleteGrid() {
        int[][] grid = new int[9][9];
        fillGrid(grid);
        return grid;
    }
    
    private boolean fillGrid(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
                    Collections.shuffle(numbers, random);
                    
                    for (int num : numbers) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (fillGrid(grid)) {
                                return true;
                            }
                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    private void removeCells(int[][] grid, int cellsToRemove) {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                positions.add(new int[]{i, j});
            }
        }
        Collections.shuffle(positions, random);
        
        for (int i = 0; i < cellsToRemove && i < positions.size(); i++) {
            int[] pos = positions.get(i);
            grid[pos[0]][pos[1]] = 0;
        }
    }
    
    private int getCellsToRemove(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY": return 35;
            case "MEDIUM": return 45;
            case "HARD": return 55;
            case "EXPERT": return 65;
            default: return 45;
        }
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
    
    private String gridToString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j] == 0 ? "." : grid[i][j]);
            }
        }
        return sb.toString();
    }
}