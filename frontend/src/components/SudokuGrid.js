import React from 'react';

const SudokuGrid = ({ grid, onCellChange, readOnly = false, highlightedCells = [] }) => {
  const handleCellChange = (index, value) => {
    if (readOnly) return;
    
    // Validate input (only digits 1-9 or empty)
    if (value === '' || (value >= '1' && value <= '9')) {
      onCellChange(index, value);
    }
  };

  const getCellClass = (index, value) => {
    let className = 'sudoku-cell';
    
    if (readOnly && value !== '0' && value !== '.') {
      className += ' given';
    }
    
    if (highlightedCells.includes(index)) {
      className += ' solved';
    }
    
    return className;
  };

  const formatValue = (value) => {
    return (value === '0' || value === '.') ? '' : value;
  };

  return (
    <div className="sudoku-grid">
      {Array.from({ length: 81 }, (_, index) => {
        const value = grid[index] || '';
        return (
          <input
            key={index}
            type="text"
            maxLength="1"
            className={getCellClass(index, value)}
            value={formatValue(value)}
            onChange={(e) => handleCellChange(index, e.target.value)}
            readOnly={readOnly}
          />
        );
      })}
    </div>
  );
};

export default SudokuGrid;