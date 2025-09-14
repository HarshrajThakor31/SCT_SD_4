package com.skillcraft.sudoku.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sudoku_puzzles")
public class SudokuPuzzle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "puzzle_grid", length = 81)
    private String puzzleGrid;
    
    @Column(name = "solution_grid", length = 81)
    private String solutionGrid;
    
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    
    @Column(name = "solve_time_ms")
    private Long solveTimeMs;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    public enum Difficulty {
        EASY, MEDIUM, HARD, EXPERT
    }

    public SudokuPuzzle() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPuzzleGrid() { return puzzleGrid; }
    public void setPuzzleGrid(String puzzleGrid) { this.puzzleGrid = puzzleGrid; }
    
    public String getSolutionGrid() { return solutionGrid; }
    public void setSolutionGrid(String solutionGrid) { this.solutionGrid = solutionGrid; }
    
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    
    public Long getSolveTimeMs() { return solveTimeMs; }
    public void setSolveTimeMs(Long solveTimeMs) { this.solveTimeMs = solveTimeMs; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getSolvedAt() { return solvedAt; }
    public void setSolvedAt(LocalDateTime solvedAt) { this.solvedAt = solvedAt; }
}