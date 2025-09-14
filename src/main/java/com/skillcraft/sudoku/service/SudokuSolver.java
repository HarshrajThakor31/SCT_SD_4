package com.skillcraft.sudoku.service;

import com.skillcraft.sudoku.dto.SudokuResponse;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SudokuSolver {
    
    public SudokuResponse solve(String gridString, String algorithm, boolean visualize) {
        int[][] grid = parseGrid(gridString);
        SudokuResponse response = new SudokuResponse();
        response.setOriginalGrid(gridString);
        response.setAlgorithm(algorithm);
        
        if (visualize) {
            response.setSteps(new ArrayList<>());
        }
        
        long startTime = System.currentTimeMillis();
        boolean solved = false;
        
        switch (algorithm.toUpperCase()) {
            case "BACKTRACKING":
                solved = solveBacktracking(grid, response);
                break;
            case "CONSTRAINT_PROPAGATION":
                solved = solveConstraintPropagation(grid, response);
                break;
            default:
                solved = solveBacktracking(grid, response);
        }
        
        long endTime = System.currentTimeMillis();
        response.setSolveTimeMs(endTime - startTime);
        response.setSolved(solved);
        
        if (solved) {
            response.setSolvedGrid(gridToString(grid));
            response.setMessage("Puzzle solved successfully!");
        } else {
            response.setMessage("Unable to solve this puzzle.");
        }
        
        return response;
    }
    
    private boolean solveBacktracking(int[][] grid, SudokuResponse response) {
        return backtrack(grid, 0, 0, response);
    }
    
    private boolean backtrack(int[][] grid, int row, int col, SudokuResponse response) {
        if (row == 9) return true;
        if (col == 9) return backtrack(grid, row + 1, 0, response);
        if (grid[row][col] != 0) return backtrack(grid, row, col + 1, response);
        
        for (int num = 1; num <= 9; num++) {
            if (isValid(grid, row, col, num)) {
                grid[row][col] = num;
                
                if (response.getSteps() != null) {
                    response.getSteps().add(new SudokuResponse.SolveStep(row, col, num, "PLACE"));
                }
                
                if (backtrack(grid, row, col + 1, response)) {
                    return true;
                }
                
                grid[row][col] = 0;
                if (response.getSteps() != null) {
                    response.getSteps().add(new SudokuResponse.SolveStep(row, col, 0, "BACKTRACK"));
                }
            }
        }
        return false;
    }
    
    private boolean solveConstraintPropagation(int[][] grid, SudokuResponse response) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (grid[row][col] == 0) {
                        Set<Integer> candidates = getCandidates(grid, row, col);
                        if (candidates.size() == 1) {
                            int value = candidates.iterator().next();
                            grid[row][col] = value;
                            changed = true;
                            
                            if (response.getSteps() != null) {
                                response.getSteps().add(new SudokuResponse.SolveStep(row, col, value, "CONSTRAINT"));
                            }
                        }
                    }
                }
            }
        }
        
        if (!isComplete(grid)) {
            return solveBacktracking(grid, response);
        }
        return true;
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
    
    private Set<Integer> getCandidates(int[][] grid, int row, int col) {
        Set<Integer> candidates = new HashSet<>();
        for (int num = 1; num <= 9; num++) {
            if (isValid(grid, row, col, num)) {
                candidates.add(num);
            }
        }
        return candidates;
    }
    
    private boolean isComplete(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) return false;
            }
        }
        return true;
    }
    
    private int[][] parseGrid(String gridString) {
        int[][] grid = new int[9][9];
        for (int i = 0; i < 81; i++) {
            char c = gridString.charAt(i);
            int value = (c == '.' || c == '0') ? 0 : Character.getNumericValue(c);
            grid[i / 9][i % 9] = value;
        }
        return grid;
    }
    
    private String gridToString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j]);
            }
        }
        return sb.toString();
    }
}