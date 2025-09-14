import React, { useState } from 'react';
import { Play, Square, RotateCcw, Download, Upload, Zap } from 'lucide-react';
import SudokuGrid from './SudokuGrid';
import { sudokuAPI } from '../services/api';
import toast, { Toaster } from 'react-hot-toast';

const SudokuSolver = () => {
  const [grid, setGrid] = useState('.'.repeat(81));

  const [solving, setSolving] = useState(false);
  const [algorithm, setAlgorithm] = useState('BACKTRACKING');
  const [difficulty, setDifficulty] = useState('MEDIUM');
  const [visualizing, setVisualizing] = useState(false);
  const [solveSteps, setSolveSteps] = useState([]);
  const [currentStep, setCurrentStep] = useState(0);
  const [highlightedCells, setHighlightedCells] = useState([]);

  const difficulties = ['EASY', 'MEDIUM', 'HARD', 'EXPERT'];
  const algorithms = ['BACKTRACKING', 'CONSTRAINT_PROPAGATION'];

  const handleCellChange = (index, value) => {
    const newGrid = grid.split('');
    newGrid[index] = value || '.';
    setGrid(newGrid.join(''));
  };

  const generatePuzzle = async () => {
    try {
      setSolving(true);
      const response = await sudokuAPI.generatePuzzle(difficulty);
      setGrid(response.puzzle);

      setSolveSteps([]);
      setCurrentStep(0);
      setHighlightedCells([]);
      toast.success(`Generated ${difficulty.toLowerCase()} puzzle!`);
    } catch (error) {
      toast.error('Failed to generate puzzle');
      console.error('Error generating puzzle:', error);
    } finally {
      setSolving(false);
    }
  };

  const solvePuzzle = async () => {
    try {
      setSolving(true);
      const response = await sudokuAPI.solvePuzzle(grid, algorithm, visualizing);
      
      if (response.solved) {
        if (visualizing && response.steps) {
          setSolveSteps(response.steps);
          setCurrentStep(0);
          animateSolution(response.steps);
        } else {
          setGrid(response.solvedGrid);
          toast.success(`Solved in ${response.solveTimeMs}ms using ${response.algorithm}!`);
        }
      } else {
        toast.error(response.message || 'Unable to solve this puzzle');
      }
    } catch (error) {
      toast.error('Failed to solve puzzle');
      console.error('Error solving puzzle:', error);
    } finally {
      setSolving(false);
    }
  };

  const animateSolution = (steps) => {
    let stepIndex = 0;
    const interval = setInterval(() => {
      if (stepIndex >= steps.length) {
        clearInterval(interval);
        toast.success('Solution complete!');
        return;
      }

      const step = steps[stepIndex];
      const cellIndex = step.row * 9 + step.col;
      
      if (step.action === 'PLACE' || step.action === 'CONSTRAINT') {
        const newGrid = grid.split('');
        newGrid[cellIndex] = step.value.toString();
        setGrid(newGrid.join(''));
        
        setHighlightedCells([cellIndex]);
        setTimeout(() => setHighlightedCells([]), 200);
      }
      
      setCurrentStep(stepIndex + 1);
      stepIndex++;
    }, 100);
  };

  const clearGrid = () => {
    setGrid('.'.repeat(81));

    setSolveSteps([]);
    setCurrentStep(0);
    setHighlightedCells([]);
    toast.success('Grid cleared!');
  };

  const validatePuzzle = async () => {
    try {
      const response = await sudokuAPI.validatePuzzle(grid);
      if (response.valid) {
        toast.success('Valid Sudoku puzzle!');
      } else {
        toast.error('Invalid Sudoku puzzle!');
      }
    } catch (error) {
      toast.error('Failed to validate puzzle');
    }
  };

  const exportPuzzle = () => {
    const dataStr = JSON.stringify({
      puzzle: grid,
      difficulty: difficulty,
      timestamp: new Date().toISOString()
    });
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);
    
    const exportFileDefaultName = `sudoku-${difficulty.toLowerCase()}-${Date.now()}.json`;
    
    const linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();
    
    toast.success('Puzzle exported!');
  };

  const importPuzzle = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        try {
          const data = JSON.parse(e.target.result);
          setGrid(data.puzzle);
          setDifficulty(data.difficulty || 'MEDIUM');
          toast.success('Puzzle imported!');
        } catch (error) {
          toast.error('Invalid file format');
        }
      };
      reader.readAsText(file);
    }
  };

  return (
    <div className="sudoku-container">
      <Toaster position="top-right" />
      
      <div className="controls">
        <div className="difficulty-selector">
          {difficulties.map(diff => (
            <button
              key={diff}
              className={`difficulty-btn ${difficulty === diff ? 'active' : ''}`}
              onClick={() => setDifficulty(diff)}
            >
              {diff}
            </button>
          ))}
        </div>
      </div>

      <SudokuGrid 
        grid={grid} 
        onCellChange={handleCellChange}
        highlightedCells={highlightedCells}
      />

      <div className="controls">
        <button 
          className="btn btn-primary" 
          onClick={generatePuzzle}
          disabled={solving}
        >
          <Square size={16} />
          Generate Puzzle
        </button>

        <select 
          value={algorithm} 
          onChange={(e) => setAlgorithm(e.target.value)}
          className="btn btn-secondary"
        >
          {algorithms.map(algo => (
            <option key={algo} value={algo}>
              {algo.replace('_', ' ')}
            </option>
          ))}
        </select>

        <label className="btn btn-secondary">
          <input
            type="checkbox"
            checked={visualizing}
            onChange={(e) => setVisualizing(e.target.checked)}
            style={{ marginRight: '0.5rem' }}
          />
          Visualize
        </label>

        <button 
          className="btn btn-success" 
          onClick={solvePuzzle}
          disabled={solving || grid === '.'.repeat(81)}
        >
          <Play size={16} />
          {solving ? 'Solving...' : 'Solve'}
        </button>

        <button 
          className="btn btn-secondary" 
          onClick={validatePuzzle}
          disabled={solving}
        >
          <Zap size={16} />
          Validate
        </button>

        <button 
          className="btn btn-secondary" 
          onClick={clearGrid}
          disabled={solving}
        >
          <RotateCcw size={16} />
          Clear
        </button>

        <button 
          className="btn btn-secondary" 
          onClick={exportPuzzle}
          disabled={grid === '.'.repeat(81)}
        >
          <Download size={16} />
          Export
        </button>
        
        <a 
          href="/SudokuSolverDesktop.jar" 
          download="SudokuSolverDesktop.jar"
          className="btn btn-secondary"
          style={{ textDecoration: 'none', display: 'inline-flex', alignItems: 'center', gap: '0.5rem' }}
        >
          🖥️ Desktop App
        </a>

        <label className="btn btn-secondary">
          <Upload size={16} />
          Import
          <input
            type="file"
            accept=".json"
            onChange={importPuzzle}
            style={{ display: 'none' }}
          />
        </label>
      </div>

      {visualizing && solveSteps.length > 0 && (
        <div className="visualization-controls">
          <span>Step {currentStep} of {solveSteps.length}</span>
          <div className="progress-bar">
            <div 
              className="progress-fill" 
              style={{ width: `${(currentStep / solveSteps.length) * 100}%` }}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default SudokuSolver;