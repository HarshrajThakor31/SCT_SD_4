package com.skillcraft.sudoku.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SudokuRequest {
    @NotNull(message = "Grid cannot be null")
    @Pattern(regexp = "^[0-9.]{81}$", message = "Grid must be 81 characters containing only digits 0-9 and dots")
    private String grid;
    
    private String algorithm = "BACKTRACKING";
    private boolean visualize = false;

    public SudokuRequest() {}

    public SudokuRequest(String grid) {
        this.grid = grid;
    }

    public String getGrid() { return grid; }
    public void setGrid(String grid) { this.grid = grid; }
    
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    
    public boolean isVisualize() { return visualize; }
    public void setVisualize(boolean visualize) { this.visualize = visualize; }
}