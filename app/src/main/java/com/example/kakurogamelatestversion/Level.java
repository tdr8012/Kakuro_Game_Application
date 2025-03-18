package com.example.kakurogamelatestversion;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
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
    static Context context; //to be able to create buttons

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

    public Template getTemplate() {
        return this.template;
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

    //to create the levels
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
                            // Now that you have templates, create the levels
                            createLevelsFromTemplates(selectedTemplates, selectedDifficulty, game);
                        }
                    } else {
                        Log.w("Level", "Error getting documents.", task.getException());
                    }
                });
    }

    private static void createLevelsFromTemplates(List<Template> templates, String difficulty, Game game) {
        // Assuming there are exactly 5 templates for each difficulty
        for (int i = 0; i < templates.size(); i++) {
            Level level = new Level(difficulty, 4, game, templates.get(i), i + 1);
        }
    }

    public static void setContext(Context c) {
        context = c;
    }

    // Create and display numbered buttons
    public static void displayLevelButtons(String selectedDifficulty) {
        LinearLayout layout = ((android.app.Activity) context).findViewById(R.id.levelButtonsLayout);
        layout.removeAllViews(); // Clear existing buttons if any

        for (int i = 1; i <= 5; i++) {
            Button button = new Button(context);
            button.setText(String.valueOf(i));
            button.setId(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 10, 10, 10); // Add margins
            params.gravity = Gravity.CENTER;
            button.setLayoutParams(params);

            final int levelNumber = i; // Capture the value of i
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLevelButtonClicked(selectedDifficulty, levelNumber);
                }
            });

            layout.addView(button);
        }
    }

    private static void onLevelButtonClicked(String selectedDifficulty, int levelNumber) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("levels")
                .whereEqualTo("difficulty", selectedDifficulty)
                .whereEqualTo("levelNumber", levelNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            Level selectedLevel = document.toObject(Level.class);
                            if (selectedLevel != null) {
                                Intent intent = new Intent(context, Gameplay.class);
                                intent.putExtra("template", selectedLevel.getTemplate());
                                intent.putExtra("difficulty", selectedLevel.getDifficulty());
                                context.startActivity(intent);
                            }
                        } else {
                            Toast.makeText(context, "Level does not exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error fetching level", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}