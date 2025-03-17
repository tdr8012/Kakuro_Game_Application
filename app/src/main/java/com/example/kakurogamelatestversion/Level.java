package com.example.kakurogamelatestversion;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Level {
    // Attributes
    private String difficulty;
    private int gridSize;
    private Game game; // A Level has-a Game to manage hints and the timer
    private Template template; // A level has-a template associated
    private int levelNumber;
    private static List<Level> allLevels = new ArrayList<>(); // Store all levels
    private static Context context; //to be able to create buttons

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

    public int getLevelNumber(){ return this.levelNumber;}

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
                Level level = new Level(levelDifficulty, 4, game, selectedTemplates.get(i), i + 1);
            }
        }
    }

    public static void setContext(Context c){
        context=c;
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
            params.gravity= Gravity.CENTER;
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
        // Get the selected Level
        Level selectedLevel = Level.getLevelByDifficultyAndNumber(selectedDifficulty, levelNumber);
        if(selectedLevel == null){
            Toast.makeText(context, "Level does not exist", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(context, Kakuro_App.class);
            intent.putExtra("gridUid", selectedLevel.getTemplate().getGridUid());
            intent.putExtra("difficulty", selectedLevel.getDifficulty());
            context.startActivity(intent);
        }
    }
}