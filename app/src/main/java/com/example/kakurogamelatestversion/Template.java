package com.example.kakurogamelatestversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    int[] grid; // Stores numbers in the grid
    Cell[] cellStates; // Represents cell states
    int[][] rowHints; // Row hints stored as a 2D array
    int[][] colHints; // Column hints stored as a 2D array
    //list to store generated templates
    private static List<Template> easyTemplates = new ArrayList<>();
    private static List<Template> mediumTemplates = new ArrayList<>();
    private static List<Template> hardTemplates = new ArrayList<>();

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

    // Method to generate multiple easy templates
    public static void generateEasyTemplates(int size, int count) {
        for (int i = 0; i < count; i++) {
            Template template = generateEasyTemplate(size);
            if (template != null && !isDuplicate(template, easyTemplates)) {
                easyTemplates.add(template);
            } else {
                i--; // Try again if template is null or a duplicate
            }
        }
    }

    // Method to generate multiple medium templates
    public static void generateMediumTemplates(int size, int count) {
        for (int i = 0; i < count; i++) {
            Template template = generateMediumTemplate(size);
            if (template != null && !isDuplicate(template, mediumTemplates)) {
                mediumTemplates.add(template);
            } else {
                i--; // Try again if template is null or a duplicate
            }
        }
    }
    // Method to generate multiple hard templates
    public static void generateHardTemplates(int size, int count) {
        for (int i = 0; i < count; i++) {
            Template template = generateHardTemplate(size);
            if (template != null && !isDuplicate(template, hardTemplates)) {
                hardTemplates.add(template);
            } else {
                i--; // Try again if template is null or a duplicate
            }
        }
    }
    //helper method to check for duplicates
    private static boolean isDuplicate(Template newTemplate, List<Template> templateList) {
        for (Template existingTemplate : templateList) {
            if (Arrays.equals(newTemplate.grid, existingTemplate.grid) &&
                    Arrays.deepEquals(newTemplate.rowHints, existingTemplate.rowHints) &&
                    Arrays.deepEquals(newTemplate.colHints, existingTemplate.colHints)) {
                return true;
            }
        }
        return false;
    }

    private static Template generateEasyTemplate(int size) {
        Template template = new Template(size);
        if (!solveKakuro(template)) {
            return null; // Failed to find a solution
        }

        removeNumbersForEasy(template);
        return template;
    }

    private static Template generateMediumTemplate(int size) {
        Template template = new Template(size);
        if (!solveKakuro(template)) {
            return null; // Failed to find a solution
        }

        removeNumbersForMedium(template);
        return template;
    }

    private static Template generateHardTemplate(int size) {
        Template template = new Template(size);
        if (!solveKakuro(template)) {
            return null; // Failed to find a solution
        }

        removeNumbersForHard(template);
        return template;
    }


    //Backtracking algorithm to solve the kakuro
    private static boolean solveKakuro(Template template) {
        for (int row = 0; row < template.size; row++) {
            for (int col = 0; col < template.size; col++) {
                if (template.grid[row * template.size + col] == 0) {
                    for (int num = 1; num <= template.size; num++) {
                        template.grid[row * template.size + col] = num;
                        if (template.validateNumber(row, col) && solveKakuro(template)) {
                            return true;
                        }
                        template.grid[row * template.size + col] = 0; // Reset if not valid
                    }
                    return false; // No valid number found
                }
            }
        }
        return true; // Grid is full and valid
    }

    // Remove numbers to create an easy puzzle
    private static void removeNumbersForEasy(Template template) {
        Random random = new Random();
        int numToRemove = (template.size * template.size) / 2; // Roughly half the cells for "easy"

        for (int i = 0; i < numToRemove; i++) {
            int row, col;
            do {
                row = random.nextInt(template.size);
                col = random.nextInt(template.size);
            } while (template.grid[row * template.size + col] == 0);

            template.grid[row * template.size + col] = 0; // Remove number
            template.cellStates[row * template.size + col] = Cell.EMPTY;

            // Recalculate hints after removing a number
            updateHints(template);
        }
    }

    // Remove numbers to create a medium puzzle (75% filled)
    private static void removeNumbersForMedium(Template template) {
        Random random = new Random();
        int totalCells = template.size * template.size;
        int numToRemove = totalCells - (int) Math.round(totalCells * 0.75);

        for (int i = 0; i < numToRemove; i++) {
            int row, col;
            do {
                row = random.nextInt(template.size);
                col = random.nextInt(template.size);
            } while (template.grid[row * template.size + col] == 0);

            template.grid[row * template.size + col] = 0; // Remove number
            template.cellStates[row * template.size + col] = Cell.EMPTY;

            // Recalculate hints after removing a number
            updateHints(template);
        }
    }
    private static void removeNumbersForHard(Template template) {
        Random random = new Random();
        int totalCells = template.size * template.size;
        int numToRemove = (int) Math.round(totalCells * 0.25);

        for (int i = 0; i < numToRemove; i++) {
            int row, col;
            do {
                row = random.nextInt(template.size);
                col = random.nextInt(template.size);
            } while (template.grid[row * template.size + col] == 0);

            template.grid[row * template.size + col] = 0; // Remove number
            template.cellStates[row * template.size + col] = Cell.EMPTY;

            // Recalculate hints after removing a number
            updateHints(template);
        }
        if (numToRemove > 0) {
            int row, col;
            do {
                row = random.nextInt(template.size);
                col = random.nextInt(template.size);
            } while (template.grid[row * template.size + col] == 0);

            template.grid[row * template.size + col] = 0; // Remove number
            template.cellStates[row * template.size + col] = Cell.EMPTY;

            // Recalculate hints after removing a number
            updateHints(template);
        }
    }
    //helper method to update hints after removing a number
    private static void updateHints(Template template) {
        for (int row = 0; row < template.size; row++) {
            int rowSum = 0;
            for (int col = 0; col < template.size; col++) {
                if (template.grid[row * template.size + col] != 0) {
                    rowSum += template.grid[row * template.size + col];
                }
            }
            Arrays.fill(template.rowHints[row], 0); // Reset row hints
            template.rowHints[row][0] = rowSum; // Update with new sum
        }

        for (int col = 0; col < template.size; col++) {
            int colSum = 0;
            for (int row = 0; row < template.size; row++) {
                if (template.grid[row * template.size + col] != 0) {
                    colSum += template.grid[row * template.size + col];
                }
            }
            Arrays.fill(template.colHints[col], 0); // Reset col hints
            template.colHints[col][0] = colSum; // Update with new sum
        }
    }

    public void loadTemplates(String difficulty) {
        //Load different puzzles from the generated list
        if(difficulty.equals("easy") && easyTemplates.isEmpty()){
            generateEasyTemplates(4, 5);
        }
        if(difficulty.equals("medium") && mediumTemplates.isEmpty()){
            generateMediumTemplates(5, 5);
        }
        if(difficulty.equals("hard") && hardTemplates.isEmpty()){
            generateHardTemplates(5, 5);
        }
        if(difficulty.equals("easy")){
            for(Template template : easyTemplates){
                //TODO load template into the game
            }
        } else if (difficulty.equals("medium")){
            for(Template template : mediumTemplates){
                //TODO load template into the game
            }
        } else if (difficulty.equals("hard")){
            for(Template template : hardTemplates){
                //TODO load template into the game
            }
        }
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

    public String getGridUid() {
        return gridUid;
    }

    public void setGridUid(String gridUid) {
        this.gridUid = gridUid;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }
}