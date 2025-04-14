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
<<<<<<< Updated upstream
        this.rowHints = new HashMap<>();
        this.colHints = new HashMap<>();
=======
        this.grid = new int[size * size];
        this.cellStates = new Cell[size * size];
        this.rowHints = new int[size][size]; // Initialize row hints
        this.colHints = new int[size][size]; // Initialize column hints
        initializeGrid();
    }

    public static Template getTemplateByUid(String gridUid) {
        return null;
    }

    private void initializeGrid() {
        for (int i = 0; i < size * size; i++) {
            grid[i] = 0; // Empty grid initially
            cellStates[i] = Cell.EMPTY;
        }
    }

    // Method to generate multiple easy templates
    public static List<Template> generateEasyTemplates(int size, int count) {
        for (int i = 0; i < count; i++) {
            Template template = generateEasyTemplate(size);
            if (template != null && !isDuplicate(template, easyTemplates)) {
                easyTemplates.add(template);
            } else {
                i--; // Try again if template is null or a duplicate
            }
        }
        return null;
    }

    // Method to generate multiple medium templates
    public static List<Template> generateMediumTemplates(int size, int count) {
        for (int i = 0; i < count; i++) {
            Template template = generateMediumTemplate(size);
            if (template != null && !isDuplicate(template, mediumTemplates)) {
                mediumTemplates.add(template);
            } else {
                i--; // Try again if template is null or a duplicate
            }
        }
        return null;
    }
    // Method to generate multiple hard templates
    public static List<Template> generateHardTemplates(int size, int count) {
        for (int i = 0; i < count; i++) {
            Template template = generateHardTemplate(size);
            if (template != null && !isDuplicate(template, hardTemplates)) {
                hardTemplates.add(template);
            } else {
                i--; // Try again if template is null or a duplicate
            }
        }
        return null;
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
    public static void createLevels(String selectedDifficulty,Game game) {
        List<Template> easyTemplates = Template.generateEasyTemplates(4, 5);
        List<Template> mediumTemplates = Template.generateMediumTemplates(4, 5);
        List<Template> hardTemplates = Template.generateHardTemplates(4, 5);

        List<Template> selectedTemplates = null;
        String levelDifficulty = null;

        switch (selectedDifficulty.toLowerCase()) {
            case "easy":
                selectedTemplates = easyTemplates;
                levelDifficulty = "easy";
                break;
            case "medium":
                selectedTemplates = mediumTemplates;
                levelDifficulty = "medium";
                break;
            case "hard":
                selectedTemplates = hardTemplates;
                levelDifficulty = "hard";
                break;
            default:
                // Handle invalid difficulty
                break;
        }
        if (selectedTemplates != null) {
            for (int i = 0; i < 5; i++) {
                Template template= selectedTemplates.get(i);
                Level level = new Level(levelDifficulty, 4, game, template, i + 1);
                template.saveToFirestore();
            }
        }
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


    public void setGrid(int[] grid){
        this.grid=grid;
    }
    public void setCellStates(Cell[] cellStates){
        this.cellStates=cellStates;
    }
    public void setRowHints(int[][] rowHints){
        this.rowHints=rowHints;
    }
    public void setColHints(int[][] colHints){
        this.colHints=colHints;
    }
    public int[] getGrid(){
        return grid;
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
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("gridUid", this.gridUid);
        templateData.put("size", this.size);
        templateData.put("isSolved", this.isSolved);
        templateData.put("grid", this.grid);
        templateData.put("cellStates", this.cellStates);
        templateData.put("rowHints", this.rowHints);
        templateData.put("colHints", this.colHints);

        db.collection("templates")
                .document(this.gridUid) // Use the gridUid as the document ID
                .set(templateData)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Template successfully written!");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error writing template: " + e);
                });
    }

    public void displayMessage(String message) {
        System.out.println(message);
>>>>>>> Stashed changes
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