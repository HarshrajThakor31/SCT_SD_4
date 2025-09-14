package com.skillcraft.sudoku.repository;

import com.skillcraft.sudoku.model.SudokuPuzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SudokuPuzzleRepository extends JpaRepository<SudokuPuzzle, Long> {
    
    List<SudokuPuzzle> findByDifficultyOrderByCreatedAtDesc(SudokuPuzzle.Difficulty difficulty);
    
    @Query("SELECT COUNT(p) FROM SudokuPuzzle p WHERE p.solvedAt IS NOT NULL")
    long countSolvedPuzzles();
    
    @Query("SELECT AVG(p.solveTimeMs) FROM SudokuPuzzle p WHERE p.solveTimeMs IS NOT NULL")
    Double getAverageSolveTime();
    
    @Query("SELECT p.difficulty, COUNT(p) FROM SudokuPuzzle p GROUP BY p.difficulty")
    List<Object[]> getPuzzleCountByDifficulty();
}