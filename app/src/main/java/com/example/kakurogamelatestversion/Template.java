package com.example.kakurogamelatestversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum Cell {
    EMPTY,    // Empty cell
    FILLED,   // User-filled cell
    FIXED     // Predefined number (not editable)
}


public class Template {
    private String gridUid;
    private Map<String, String> rowHints;
    private Map<String, String> colHints;
    private int size;
    private boolean isSolved;
    private String difficulty;
    private String[][] grid;

    public Template() {
        // Needed for Firebase
    }

    public Template(String gridUid, String[][] grid, String difficulty) {
        this.gridUid = gridUid;
        this.grid = grid;
        this.difficulty = difficulty;
        this.size = grid.length;
        this.isSolved = false;
        this.rowHints = new HashMap<>();
        this.colHints = new HashMap<>();
    }

    // Getters and setters
    public String getGridUid() {
        return gridUid;
    }

    public void setGridUid(String gridUid) {
        this.gridUid = gridUid;
    }

    public String[][] getGrid() {
        return grid;
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public Map<String, String> getRowHints() {
        return rowHints;
    }

    public void setRowHints(Map<String, String> rowHints) {
        this.rowHints = rowHints;
    }

    public Map<String, String> getColHints() {
        return colHints;
    }

    public void setColHints(Map<String, String> colHints) {
        this.colHints = colHints;
    }

    public static List<Template> loadTemplates() {
        List<Template> templates = new ArrayList<>();

        // Easy templates
        templates.add(new Template("easy1", new String[][]{
                {"/", "⬍6", "⬍5", "/"},
                {"➞7", "", "", "/"},
                {"➞4", "", "", "/"},
                {"/", "/", "/", "/"}
        }, "easy"));

        templates.add(new Template("easy2", new String[][]{
                {"/", "⬍11", "⬍4", "/"},
                {"➞12", "", "", "/"},
                {"➞3", "", "", "/"},
                {"/", "/", "/", "/"}
        }, "easy"));

        templates.add(new Template("easy3", new String[][]{
                {"/", "⬍17", "⬍16", "/"},
                {"➞17", "", "", "/"},
                {"➞16", "", "", "/"},
                {"/", "/", "/", "/"}
        }, "easy"));

        templates.add(new Template("easy4", new String[][]{
                {"/", "⬍15", "⬍7", "/"},
                {"➞15", "", "", "/"},
                {"➞7", "", "", "/"},
                {"/", "/", "/", "/"}
        }, "easy"));

        templates.add(new Template("easy5", new String[][]{
                {"/", "⬍12", "⬍9", "/"},
                {"➞17", "", "", "/"},
                {"➞4", "", "", "/"},
                {"/", "/", "/", "/"}
        }, "easy"));

        // Medium templates
        templates.add(new Template("medium1", new String[][]{
                {"/", "⬍17", "⬍9", "/"},
                {"➞17", "", "", "/"},
                {"➞16", "", "", "/"},
                {"/", "/", "/", "/"}
        }, "medium"));

        templates.add(new Template("medium2", new String[][]{
                {"/", "⬍21", "⬍11", "⬍10"},
                {"➞23", "", "", ""},
                {"➞12", "", "", ""},
                {"➞7", "", "", ""}
        }, "medium"));

        templates.add(new Template("medium3", new String[][]{
                {"/", "⬍20", "⬍15", "⬍10"},
                {"➞23", "", "", ""},
                {"➞15", "", "", ""},
                {"➞7", "", "", ""}
        }, "medium"));

        templates.add(new Template("medium4", new String[][]{
                {"/", "⬍23", "⬍13", "⬍9"},
                {"➞23", "", "", ""},
                {"➞12", "", "", ""},
                {"➞10", "", "", ""}
        }, "medium"));

        templates.add(new Template("medium5", new String[][]{
                {"/", "⬍24", "⬍17", "⬍7"},
                {"➞20", "", "", ""},
                {"➞18", "", "", ""},
                {"➞10", "", "", ""}
        }, "medium"));

        // Hard templates
        templates.add(new Template("hard1", new String[][]{
                {"/", "⬍20", "⬍11", "⬍7"},
                {"➞20", "", "", ""},
                {"➞12", "", "", ""},
                {"➞6", "", "", ""}
        }, "hard"));

        templates.add(new Template("hard2", new String[][]{
                {"/", "⬍23", "⬍15", "⬍10"},
                {"➞24", "", "", ""},
                {"➞14", "", "", ""},
                {"➞10", "", "", ""}
        }, "Hard"));

        templates.add(new Template("hard3", new String[][]{
                {"/", "⬍23", "⬍20", "⬍7"},
                {"➞20", "", "", ""},
                {"➞19", "", "", ""},
                {"➞11", "", "", ""}
        }, "hard"));

        templates.add(new Template("hard4", new String[][]{
                {"/", "⬍24", "⬍17", "⬍7"},
                {"➞20", "", "", ""},
                {"➞18", "", "", ""},
                {"➞10", "", "", ""}
        }, "hard"));

        templates.add(new Template("hard5", new String[][]{
                {"/", "⬍21", "⬍20", "⬍10"},
                {"➞23", "", "", ""},
                {"➞19", "", "", ""},
                {"➞9", "", "", ""}
        }, "hard"));

        return templates;
    }
}