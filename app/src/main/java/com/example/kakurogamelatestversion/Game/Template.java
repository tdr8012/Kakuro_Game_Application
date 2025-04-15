package com.example.kakurogamelatestversion.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Template {
    public String gridUid;
    public Map<Position, String> rowHints = new HashMap<>();
    public Map<Position, String> colHints = new HashMap<>();
    public String[][] grid; // 2D array for display and clues
    public int size;
    public boolean isSolved;
    public Cell[] cellStates;
    private String difficulty; // Optional: to store Easy/Medium/Hard

    // Default constructor (needed for Firestore)
    public Template() {}

    // Optional constructor for easy instantiation
    public Template(String gridUid, Map<Position, String> rowHints,
                    Map<Position, String> colHints, String[][] grid,
                    int size, boolean isSolved, Cell[] cellStates) {
        this.gridUid = gridUid;
        this.rowHints = rowHints;
        this.colHints = colHints;
        this.grid = grid;
        this.size = size;
        this.isSolved = isSolved;
        this.cellStates = cellStates;
    }

    // Getter and Setter for difficulty
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // --- Methods ---
    public void loadTemplates() {
        // You can implement loading logic from Firestore here
    }

    public boolean insertNumber(int row, int col, int number) {
        int index = row * size + col;
        if (cellStates == null || index >= cellStates.length) return false;

        Cell cell = cellStates[index];
        if (validateNumber(row, col, number)) {
            cell.setValue(number);
            return true;
        }
        return false;
    }

    public void setNumber(int row, int col, int number) {
        int index = row * size + col;
        if (cellStates != null && index < cellStates.length) {
            cellStates[index].setValue(number);
        }
    }

    public boolean validateNumber(int row, int col, int number) {
        // Basic validation; more logic can be added later
        return number >= 1 && number <= 9;
    }

    public String displayMessage() {
        return isSolved ? "Puzzle completed!" : "Keep going!";
    }

    public void extractHintsFromGrid() {
        if (grid == null) return;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                String cell = grid[row][col];
                if (cell.startsWith("➞")) {
                    rowHints.put(new Position(row, col), cell.substring(1)); // e.g. "➞17"
                } else if (cell.startsWith("⬍")) {
                    colHints.put(new Position(row, col), cell.substring(1)); // e.g. "⬍12"
                }
            }
        }
    }

    public boolean getGridUid() {
        return false;
    }


    public static class Cell {
        private int value;

        public Cell() {
            value = 0;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class Position {
        public int row;
        public int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
}
