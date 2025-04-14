package com.example.kakurogamelatestversion;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class Level {
    // Attributes
    private String difficulty;
    private int gridSize;
    private Game game; // A Level has-a Game to manage hints and the timer
    private Template template; // A level has-a template associated
    private int levelNumber;
    private static List<Level> allLevels = new ArrayList<>(); // Store all levels
    static Context context; // to be able to create buttons

    // Constructor
    public Level(String difficulty, int gridSize, Game game, Template template, int levelNumber) {
        this.difficulty = difficulty;
        this.gridSize = gridSize;
        this.game = game; // Pass the Game object from outside
        this.template = template; // Pass the template object from outside
        this.levelNumber = levelNumber;
        allLevels.add(this); // Add this level to the list of all levels
    }

    // Methods
    // Setters
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public void setLevelDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    // Getters
    public String getDifficulty() {
        return this.difficulty;
    }

    public int getGridSize() {
        return this.gridSize;
    }

    public Game getGame() {
        return this.game;
    }

    public boolean getTemplate() {
        return this.template.isSolved();
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    // Find level by difficulty and level number
    public static Level getLevelByDifficultyAndNumber(String difficulty, int levelNumber) {
        for (Level level : allLevels) {
            if (level.getDifficulty().equalsIgnoreCase(difficulty) && level.getLevelNumber() == levelNumber) {
                return level;
            }
        }
        return null; // Level not found
    }

    // Create levels using templates from Firestore
    public static void createLevels(String selectedDifficulty, Game game) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch the templates based on difficulty from Firestore
        db.collection("templates")
                .whereEqualTo("difficulty", selectedDifficulty)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            List<Template> selectedTemplates = new ArrayList<>();
                            for (DocumentSnapshot document : querySnapshot) {
                                // Convert each document to a Template object
                                Template template = document.toObject(Template.class);
                                if (template != null) {
                                    selectedTemplates.add(template);
                                }
                            }
                            // Create levels from the templates
                            createLevelsFromTemplates(selectedTemplates, selectedDifficulty, game);
                        }
                    } else {
                        Log.w("Level", "Error getting documents.", task.getException());
                    }
                });
    }

    // Create levels from the templates
    private static void createLevelsFromTemplates(List<Template> templates, String difficulty, Game game) {
        for (int i = 0; i < templates.size(); i++) {
            Template template = templates.get(i);
            int gridSize = template.getGrid().length; // Get grid size from the template
            Level level = new Level(difficulty, gridSize, game, template, i + 1);
        }
    }

    public static void setContext(Context c) {
        context = c;
    }

    // Create and display numbered buttons
    public static void displayLevelButtons(String selectedDifficulty) {
        LinearLayout layout = ((android.app.Activity) context).findViewById(R.id.levelButtonsLayout);
        layout.removeAllViews(); // Clear existing buttons if any

        // Fetch levels based on selected difficulty
        List<Level> levels = getLevelsByDifficulty(selectedDifficulty);

        for (Level level : levels) {
            Button button = new Button(context);
            button.setText(String.valueOf(level.getLevelNumber()));
            button.setId(level.getLevelNumber());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 10, 10, 10); // Add margins
            params.gravity = Gravity.CENTER;
            button.setLayoutParams(params);

            button.setOnClickListener(v -> onLevelButtonClicked(level));

            layout.addView(button);
        }
    }

    // Fetch levels by difficulty
    public static List<Level> getLevelsByDifficulty(String difficulty) {
        List<Level> levels = new ArrayList<>();
        for (Level level : allLevels) {
            if (level.getDifficulty().equalsIgnoreCase(difficulty)) {
                levels.add(level);
            }
        }
        return levels;
    }

    // Handle level button click
    private static void onLevelButtonClicked(Level level) {
        Intent intent = new Intent(context, Gameplay.class);
        intent.putExtra("template", level.getTemplate());
        intent.putExtra("difficulty", level.getDifficulty());
        context.startActivity(intent);
    }
}