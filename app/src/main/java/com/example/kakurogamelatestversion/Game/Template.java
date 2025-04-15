package com.example.kakurogamelatestversion.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Template {
    public List<List<String>> grid;
    public List<List<Integer>> solution;
    public int size;
    public boolean isSolved;
    public List<Cell> cellStates;
    public Map<String, String> rowHints = new HashMap<>();
    public Map<String, String> colHints = new HashMap<>();

    public void extractHintsFromGrid() {
        if (grid == null) return;

        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.get(row).size(); col++) {
                String cell = grid.get(row).get(col);
                String key = row + "_" + col;
                if (cell.startsWith("➞")) {
                    rowHints.put(key, cell.substring(1));
                } else if (cell.startsWith("⬍")) {
                    colHints.put(key, cell.substring(1));
                }
            }
        }
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
}
