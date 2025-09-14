package com.skillcraft.sudoku.desktop.ui;

import com.skillcraft.sudoku.desktop.SimpleSudokuSolver;
import com.skillcraft.sudoku.desktop.SimpleSudokuGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuDashboard extends JFrame {
    private SimpleSudokuSolver solver;
    private SimpleSudokuGenerator generator;
    private JTextField[][] gridCells;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JComboBox<String> difficultyCombo;
    private JComboBox<String> algorithmCombo;
    
    public SudokuDashboard() {
        solver = new SimpleSudokuSolver();
        generator = new SimpleSudokuGenerator();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Sudoku Solver Pro - Desktop Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel with Grid and Controls
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Sudoku Grid Container - Fixed size
        JPanel gridContainer = new JPanel();
        gridContainer.setLayout(new BoxLayout(gridContainer, BoxLayout.Y_AXIS));
        gridContainer.setBackground(new Color(248, 249, 250));
        
        JPanel gridPanel = createSudokuGrid();
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridContainer.add(Box.createVerticalGlue());
        gridContainer.add(gridPanel);
        gridContainer.add(Box.createVerticalGlue());
        
        mainPanel.add(gridContainer, BorderLayout.CENTER);
        
        // Control Panel - Fixed at bottom
        JPanel controlPanel = createControlPanel();
        controlPanel.setPreferredSize(new Dimension(800, 120));
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Status Panel
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(67, 56, 202));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("🧩 Sudoku Solver Pro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.WEST);
        
        JLabel subtitleLabel = new JLabel("Professional Sudoku Solving Dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(196, 181, 253));
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSudokuGrid() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        container.setPreferredSize(new Dimension(580, 580));
        container.setMaximumSize(new Dimension(580, 580));
        
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 1, 1));
        gridPanel.setBackground(new Color(55, 65, 81));
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(55, 65, 81), 3));
        gridPanel.setPreferredSize(new Dimension(550, 550));
        
        gridCells = new JTextField[9][9];
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Segoe UI", Font.BOLD, 20));
                cell.setPreferredSize(new Dimension(58, 58));
                cell.setMinimumSize(new Dimension(58, 58));
                cell.setMaximumSize(new Dimension(58, 58));
                cell.setBackground(Color.WHITE);
                cell.setForeground(new Color(31, 41, 55));
                
                // Modern borders for 3x3 boxes
                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row % 3 == 2) ? 3 : 1;
                int right = (col % 3 == 2) ? 3 : 1;
                
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, new Color(55, 65, 81)));
                
                // Hover effect
                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        if (cell.isEditable()) {
                            cell.setBackground(new Color(243, 244, 246));
                        }
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        if (cell.isEditable() && !cell.getBackground().equals(new Color(212, 237, 218))) {
                            cell.setBackground(Color.WHITE);
                        }
                    }
                });
                
                gridCells[row][col] = cell;
                gridPanel.add(cell);
            }
        }
        
        container.add(gridPanel, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Settings Panel
        JPanel settingsPanel = new JPanel(new FlowLayout());
        settingsPanel.setBackground(new Color(248, 249, 250));
        
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        settingsPanel.add(diffLabel);
        
        difficultyCombo = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD", "EXPERT"});
        difficultyCombo.setSelectedItem("MEDIUM");
        difficultyCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        difficultyCombo.setPreferredSize(new Dimension(120, 35));
        settingsPanel.add(difficultyCombo);
        
        settingsPanel.add(Box.createHorizontalStrut(20));
        
        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        settingsPanel.add(algoLabel);
        
        algorithmCombo = new JComboBox<>(new String[]{"BACKTRACKING", "CONSTRAINT_PROPAGATION"});
        algorithmCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        algorithmCombo.setPreferredSize(new Dimension(180, 35));
        settingsPanel.add(algorithmCombo);
        
        panel.add(settingsPanel);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(new Color(248, 249, 250));
        
        JButton generateBtn = createModernButton("🎲 Generate Puzzle", new Color(67, 56, 202), Color.WHITE);
        generateBtn.addActionListener(e -> generatePuzzle());
        buttonsPanel.add(generateBtn);
        
        JButton solveBtn = createModernButton("⚡ Solve", new Color(16, 185, 129), Color.WHITE);
        solveBtn.addActionListener(e -> solvePuzzle());
        buttonsPanel.add(solveBtn);
        
        JButton validateBtn = createModernButton("✓ Validate", new Color(245, 158, 11), Color.WHITE);
        validateBtn.addActionListener(e -> validatePuzzle());
        buttonsPanel.add(validateBtn);
        
        JButton clearBtn = createModernButton("🗑 Clear", new Color(239, 68, 68), Color.WHITE);
        clearBtn.addActionListener(e -> clearGrid());
        buttonsPanel.add(clearBtn);
        
        panel.add(buttonsPanel);
        
        return panel;
    }
    
    private JButton createModernButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 45));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        statusLabel = new JLabel("Ready to solve puzzles");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(209, 213, 219));
        panel.add(statusLabel, BorderLayout.WEST);
        
        timeLabel = new JLabel("⏱ Time: 0ms");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(new Color(34, 197, 94));
        panel.add(timeLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void generatePuzzle() {
        try {
            statusLabel.setText("Generating puzzle...");
            String difficulty = (String) difficultyCombo.getSelectedItem();
            String puzzle = generator.generatePuzzle(difficulty);
            setGridFromString(puzzle);
            
            statusLabel.setText("Puzzle generated successfully!");
        } catch (Exception e) {
            statusLabel.setText("Error generating puzzle: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error generating puzzle: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void solvePuzzle() {
        try {
            statusLabel.setText("Solving puzzle...");
            String gridString = getGridAsString();
            int[][] grid = parseGrid(gridString);
            
            long startTime = System.currentTimeMillis();
            boolean solved = solver.solve(grid);
            long endTime = System.currentTimeMillis();
            
            if (solved) {
                String solvedGrid = gridToString(grid);
                setGridFromString(solvedGrid);
                statusLabel.setText("Puzzle solved successfully!");
                timeLabel.setText("Time: " + (endTime - startTime) + "ms");
                
                // Highlight solved cells
                highlightSolvedCells(gridString, solvedGrid);
            } else {
                statusLabel.setText("Unable to solve this puzzle");
                JOptionPane.showMessageDialog(this, "Unable to solve this puzzle", 
                                            "Solve Failed", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            statusLabel.setText("Error solving puzzle: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error solving puzzle: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                gridCells[row][col].setText("");
                gridCells[row][col].setBackground(Color.WHITE);
                gridCells[row][col].setEditable(true);
            }
        }
        statusLabel.setText("Grid cleared");
        timeLabel.setText("Time: 0ms");
    }
    
    private void validatePuzzle() {
        try {
            String gridString = getGridAsString();
            boolean isValid = isValidSudoku(gridString);
            
            if (isValid) {
                statusLabel.setText("Valid Sudoku puzzle!");
                JOptionPane.showMessageDialog(this, "Valid Sudoku puzzle!", 
                                            "Validation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("Invalid Sudoku puzzle!");
                JOptionPane.showMessageDialog(this, "Invalid Sudoku puzzle!", 
                                            "Validation", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            statusLabel.setText("Error validating puzzle");
            JOptionPane.showMessageDialog(this, "Error validating puzzle: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getGridAsString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = gridCells[row][col].getText().trim();
                if (text.isEmpty() || text.equals("0")) {
                    sb.append(".");
                } else {
                    sb.append(text);
                }
            }
        }
        return sb.toString();
    }
    
    private void setGridFromString(String gridString) {
        for (int i = 0; i < 81 && i < gridString.length(); i++) {
            int row = i / 9;
            int col = i % 9;
            char c = gridString.charAt(i);
            
            if (c == '.' || c == '0') {
                gridCells[row][col].setText("");
            } else {
                gridCells[row][col].setText(String.valueOf(c));
            }
        }
    }
    
    private void highlightSolvedCells(String original, String solved) {
        for (int i = 0; i < 81; i++) {
            int row = i / 9;
            int col = i % 9;
            
            char origChar = i < original.length() ? original.charAt(i) : '.';
            char solvedChar = i < solved.length() ? solved.charAt(i) : '.';
            
            if ((origChar == '.' || origChar == '0') && solvedChar != '.' && solvedChar != '0') {
                gridCells[row][col].setBackground(new Color(212, 237, 218));
                gridCells[row][col].setEditable(false);
            } else if (origChar != '.' && origChar != '0') {
                gridCells[row][col].setBackground(new Color(248, 249, 250));
                gridCells[row][col].setEditable(false);
            }
        }
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
        // Check rows
        for (int row = 0; row < 9; row++) {
            boolean[] used = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int val = grid[row][col];
                if (val != 0) {
                    if (used[val]) return false;
                    used[val] = true;
                }
            }
        }
        
        // Check columns
        for (int col = 0; col < 9; col++) {
            boolean[] used = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int val = grid[row][col];
                if (val != 0) {
                    if (used[val]) return false;
                    used[val] = true;
                }
            }
        }
        
        // Check 3x3 boxes
        for (int box = 0; box < 9; box++) {
            boolean[] used = new boolean[10];
            int startRow = (box / 3) * 3;
            int startCol = (box % 3) * 3;
            
            for (int row = startRow; row < startRow + 3; row++) {
                for (int col = startCol; col < startCol + 3; col++) {
                    int val = grid[row][col];
                    if (val != 0) {
                        if (used[val]) return false;
                        used[val] = true;
                    }
                }
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