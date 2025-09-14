package com.skillcraft.sudoku.controller;

import com.skillcraft.sudoku.dto.*;
import com.skillcraft.sudoku.model.SudokuPuzzle;
import com.skillcraft.sudoku.service.*;
import com.skillcraft.sudoku.repository.SudokuPuzzleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "*")
public class SudokuController {
    
    @Autowired
    private SudokuSolver sudokuSolver;
    
    @Autowired
    private SudokuGenerator sudokuGenerator;
    
    @Autowired
    private SudokuPuzzleRepository puzzleRepository;
    
    @PostMapping("/solve")
    public ResponseEntity<SudokuResponse> solvePuzzle(@Valid @RequestBody SudokuRequest request) {
        try {
            SudokuResponse response = sudokuSolver.solve(
                request.getGrid(), 
                request.getAlgorithm(), 
                request.isVisualize()
            );
            
            // Save to database
            SudokuPuzzle puzzle = new SudokuPuzzle();
            puzzle.setPuzzleGrid(request.getGrid());
            puzzle.setSolutionGrid(response.getSolvedGrid());
            puzzle.setSolveTimeMs(response.getSolveTimeMs());
            if (response.isSolved()) {
                puzzle.setSolvedAt(LocalDateTime.now());
            }
            puzzleRepository.save(puzzle);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            SudokuResponse errorResponse = new SudokuResponse();
            errorResponse.setSolved(false);
            errorResponse.setMessage("Error solving puzzle: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/generate")
    public ResponseEntity<Map<String, String>> generatePuzzle(
            @RequestParam(defaultValue = "MEDIUM") String difficulty) {
        try {
            SudokuPuzzle.Difficulty diff = SudokuPuzzle.Difficulty.valueOf(difficulty.toUpperCase());
            String puzzle = sudokuGenerator.generatePuzzle(diff);
            
            // Save generated puzzle
            SudokuPuzzle puzzleEntity = new SudokuPuzzle();
            puzzleEntity.setPuzzleGrid(puzzle);
            puzzleEntity.setDifficulty(diff);
            puzzleRepository.save(puzzleEntity);
            
            Map<String, String> response = new HashMap<>();
            response.put("puzzle", puzzle);
            response.put("difficulty", difficulty);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error generating puzzle: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validatePuzzle(@RequestParam String grid) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isValid = isValidSudoku(grid);
            response.put("valid", isValid);
            response.put("message", isValid ? "Valid Sudoku puzzle" : "Invalid Sudoku puzzle");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Error validating puzzle: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalPuzzles = puzzleRepository.count();
        long solvedPuzzles = puzzleRepository.countSolvedPuzzles();
        Double avgSolveTime = puzzleRepository.getAverageSolveTime();
        
        stats.put("totalPuzzles", totalPuzzles);
        stats.put("solvedPuzzles", solvedPuzzles);
        stats.put("averageSolveTimeMs", avgSolveTime != null ? avgSolveTime.longValue() : 0);
        stats.put("successRate", totalPuzzles > 0 ? (double) solvedPuzzles / totalPuzzles * 100 : 0);
        
        List<Object[]> difficultyStats = puzzleRepository.getPuzzleCountByDifficulty();
        Map<String, Long> difficultyMap = new HashMap<>();
        for (Object[] stat : difficultyStats) {
            difficultyMap.put(stat[0].toString(), (Long) stat[1]);
        }
        stats.put("puzzlesByDifficulty", difficultyMap);
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<SudokuPuzzle>> getPuzzleHistory(
            @RequestParam(defaultValue = "10") int limit) {
        List<SudokuPuzzle> puzzles = puzzleRepository.findAll()
            .stream()
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .limit(limit)
            .toList();
        return ResponseEntity.ok(puzzles);
    }
    
    private boolean isValidSudoku(String gridString) {
        if (gridString.length() != 81) return false;
        
        int[][] grid = new int[9][9];
        for (int i = 0; i < 81; i++) {
            char c = gridString.charAt(i);
            if (c != '.' && c != '0' && (c < '1' || c > '9')) return false;
            grid[i / 9][i % 9] = (c == '.' || c == '0') ? 0 : Character.getNumericValue(c);
        }
        
        return isValidGrid(grid);
    }
    
    private boolean isValidGrid(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            if (!isValidRow(grid, i) || !isValidCol(grid, i) || !isValidBox(grid, i)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isValidRow(int[][] grid, int row) {
        Set<Integer> seen = new HashSet<>();
        for (int col = 0; col < 9; col++) {
            int val = grid[row][col];
            if (val != 0 && !seen.add(val)) return false;
        }
        return true;
    }
    
    private boolean isValidCol(int[][] grid, int col) {
        Set<Integer> seen = new HashSet<>();
        for (int row = 0; row < 9; row++) {
            int val = grid[row][col];
            if (val != 0 && !seen.add(val)) return false;
        }
        return true;
    }
    
    private boolean isValidBox(int[][] grid, int boxIndex) {
        Set<Integer> seen = new HashSet<>();
        int startRow = (boxIndex / 3) * 3;
        int startCol = (boxIndex % 3) * 3;
        
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                int val = grid[i][j];
                if (val != 0 && !seen.add(val)) return false;
            }
        }
        return true;
    }
}