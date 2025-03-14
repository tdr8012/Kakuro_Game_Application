package com.example.kakurogamelatestversion;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

enum Cell {
    EMPTY,    // Empty cell
    FILLED,   // User-filled cell
    FIXED     // Predefined number (not editable)
}

class Template {
    private String gridUid;
    private int size;
    private boolean isSolved;
    private int[] grid; // Stores numbers in the grid
    private Cell[] cellStates; // Represents cell states
    private int[][] rowHints; // Row hints stored as a 2D array
    private int[][] colHints; // Column hints stored as a 2D array

    public Template(int size) {
        this.gridUid = UUID.randomUUID().toString();
        this.size = size;
        this.isSolved = false;
        this.grid = new int[size * size];
        this.cellStates = new Cell[size * size];
        this.rowHints = new int[size][size]; // Initialize row hints
        this.colHints = new int[size][size]; // Initialize column hints
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < size * size; i++) {
            grid[i] = 0; // Empty grid initially
            cellStates[i] = Cell.EMPTY;
        }
    }

    public void loadTemplates() {
        // Load different puzzles from a database or predefined list
        
    }

    public void insertNumber(int row, int col, int number) {
        int index = row * size + col;
        if (cellStates[index] == Cell.FIXED) {
            displayMessage("Cannot modify a fixed cell!");
            return;
        }
        grid[index] = number;
        cellStates[index] = Cell.FILLED;
        validateNumber(row, col);
    }

    public void setNumber(int row, int col, int number) {
        int index = row * size + col;
        grid[index] = number;
    }

    public boolean validateNumber(int row, int col) {
        int rowSum = 0, colSum = 0;
        boolean[] rowNumbers = new boolean[size + 1];
        boolean[] colNumbers = new boolean[size + 1];

        // Validate row
        for (int i = 0; i < size; i++) {
            int num = grid[row * size + i];
            if (num != 0) {
                if (rowNumbers[num]) {
                    displayMessage("Duplicate number in row!");
                    return false;
                }
                rowNumbers[num] = true;
                rowSum += num;
            }
        }

        // Validate column
        for (int i = 0; i < size; i++) {
            int num = grid[i * size + col];
            if (num != 0) {
                if (colNumbers[num]) {
                    displayMessage("Duplicate number in column!");
                    return false;
                }
                colNumbers[num] = true;
                colSum += num;
            }
        }

        boolean isRowValid = rowHints[row][col] == 0 || rowSum <= rowHints[row][col];
        boolean isColValid = colHints[row][col] == 0 || colSum <= colHints[row][col];

        if (!isRowValid || !isColValid) {
            displayMessage("Invalid number! Sum exceeds hint.");
            return false;
        }

        return true;
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
}