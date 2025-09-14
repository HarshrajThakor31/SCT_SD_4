package com.skillcraft.sudoku.desktop;

import com.skillcraft.sudoku.desktop.ui.SudokuDashboard;
import javax.swing.*;

public class SudokuDesktopApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SudokuDashboard().setVisible(true);
        });
    }
}