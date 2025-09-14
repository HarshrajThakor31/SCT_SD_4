package com.skillcraft.sudoku.dto;

import java.util.List;

public class SudokuResponse {
    private boolean solved;
    private String originalGrid;
    private String solvedGrid;
    private long solveTimeMs;
    private String algorithm;
    private int iterations;
    private List<SolveStep> steps;
    private String message;

    public static class SolveStep {
        private int row;
        private int col;
        private int value;
        private String action;
        private long timestamp;

        public SolveStep(int row, int col, int value, String action) {
            this.row = row;
            this.col = col;
            this.value = value;
            this.action = action;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters and Setters
        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }
        
        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }
        
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
        
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    // Getters and Setters
    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }
    
    public String getOriginalGrid() { return originalGrid; }
    public void setOriginalGrid(String originalGrid) { this.originalGrid = originalGrid; }
    
    public String getSolvedGrid() { return solvedGrid; }
    public void setSolvedGrid(String solvedGrid) { this.solvedGrid = solvedGrid; }
    
    public long getSolveTimeMs() { return solveTimeMs; }
    public void setSolveTimeMs(long solveTimeMs) { this.solveTimeMs = solveTimeMs; }
    
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    
    public int getIterations() { return iterations; }
    public void setIterations(int iterations) { this.iterations = iterations; }
    
    public List<SolveStep> getSteps() { return steps; }
    public void setSteps(List<SolveStep> steps) { this.steps = steps; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}