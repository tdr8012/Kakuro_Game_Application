package com.example.kakurogamelatestversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Gameplay extends AppCompatActivity {

    private Game game;
    private Template template;
    private String difficulty;
    private FirebaseFirestore db;
    private TextView selectedCell;
    private TextView messageTextView;
    private Button dashboardButton;
    private CountDownTimer countDownTimer;
    private boolean isGameOver = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        db = FirebaseFirestore.getInstance();
        game = new Game(); // Initialize the game object

       // messageTextView = findViewById(R.id.messageTextView); // TextView for displaying the message
       // dashboardButton = findViewById(R.id.dashboardButton); // Button to redirect to the dashboard
        dashboardButton.setVisibility(View.INVISIBLE); // Initially hidden

        // Get data passed from Level
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String gridUid = extras.getString("gridUid");
            difficulty = extras.getString("difficulty");
            // Now you can use gridUid and difficulty to load the game
            loadTemplateFromFirestore(gridUid);
        }
    }

    private void loadTemplateFromFirestore(String gridUid) {
        db.collection("templates")
                .document(gridUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Convert the Firestore data to a Template object
                            template = convertDocumentToTemplate(document);

                            // Start the game now that the template is loaded
                            startGame();
                        } else {
                            // Handle the case where the document doesn't exist
                            Toast.makeText(this, "Template not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(this, "Failed to load template", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Template convertDocumentToTemplate(DocumentSnapshot document) {
        Template template = new Template(document.getLong("size").intValue());
        template.setGridUid(document.getString("gridUid"));
        template.setSolved(Boolean.TRUE.equals(document.getBoolean("isSolved")));

        // Load the grid
        java.util.List<Long> gridLongList = (java.util.List<Long>) document.get("grid");
        assert gridLongList != null;
        int[] grid = new int[gridLongList.size()];
        for (int i = 0; i < gridLongList.size(); i++) {
            grid[i] = gridLongList.get(i).intValue();
        }
        template.setGrid(grid);

        // Load the cellStates
        java.util.List<String> cellStatesStringList = (java.util.List<String>) document.get("cellStates");
        assert cellStatesStringList != null;
        Cell[] cellStates = new Cell[cellStatesStringList.size()];
        for (int i = 0; i < cellStatesStringList.size(); i++) {
            cellStates[i] = Cell.valueOf(cellStatesStringList.get(i));
        }
        template.setCellStates(cellStates);

        // Load rowHints
        java.util.List<java.util.List<Long>> rowHintsLongList = (java.util.List<java.util.List<Long>>) document.get("rowHints");
        assert rowHintsLongList != null;
        int[][] rowHints = new int[rowHintsLongList.size()][];
        for (int i = 0; i < rowHintsLongList.size(); i++) {
            java.util.List<Long> rowHint = rowHintsLongList.get(i);
            rowHints[i] = new int[rowHint.size()];
            for (int j = 0; j < rowHint.size(); j++) {
                rowHints[i][j] = rowHint.get(j).intValue();
            }
        }
        template.setRowHints(rowHints);

        // Load colHints
        java.util.List<java.util.List<Long>> colHintsLongList = (java.util.List<java.util.List<Long>>) document.get("colHints");
        assert colHintsLongList != null;
        int[][] colHints = new int[colHintsLongList.size()][];
        for (int i = 0; i < colHintsLongList.size(); i++) {
            java.util.List<Long> colHint = colHintsLongList.get(i);
            colHints[i] = new int[colHint.size()];
            for (int j = 0; j < colHint.size(); j++) {
                colHints[i][j] = colHint.get(j).intValue();
            }
        }
        template.setColHints(colHints);

        return template;
    }

    public void startGame() {
        // Set up the game based on difficulty
        switch (difficulty) {
            case "Easy":
                game.setTimerOff(); // No timer for Easy
                game.setHintOn(); // Enable hints for Easy
                break;

            case "Medium":
                game.setTimerOn(); // Enable timer for Medium
                game.setHintOn(); // Enable hints for Medium
                startTimer(5 * 60 * 1000); // 5 minutes timer
                break;

            case "Hard":
                game.setTimerOn(); // Enable timer for Hard
                game.setHintOff(); // Disable hints for Hard
                startTimer(3 * 60 * 1000); // 3 minutes timer
                break;

            default:
                game.setTimerOff(); // Default: No timer
                game.setHintOff(); // Default: No hints
                break;
        }

        // Initialize the grid
        int[] grid = template.getGrid();
        GridLayout gridLayout = findViewById(R.id.kakuroGridLayout);
        gridLayout.setColumnCount(template.getSize()); // Set the correct column count
        gridLayout.setRowCount(template.getSize()); // Set the correct row count

        // Create all the cells
        for (int i = 0; i < template.getGrid().length; i++) {
            TextView cell = new TextView(this);
            cell.setText(String.valueOf(template.getGrid()[i]));
            // Set the cell depending on the type
            // TODO: Set up cell selection
            gridLayout.addView(cell);
        }

        // Set up number buttons (you can add listeners here as well)
        setUpNumberButtons();
    }

    private void startTimer(long timeInMillis) {
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update timer display here if needed
            }

            @Override
            public void onFinish() {
                if (!isGameOver) {
                    endGame(false); // End game if time runs out
                }
            }
        };
        countDownTimer.start();
    }

    private void setUpNumberButtons() {
        Button numberButton1 = findViewById(R.id.numberButton1);
        Button numberButton2 = findViewById(R.id.numberButton2);
        Button numberButton3 = findViewById(R.id.numberButton3);
        Button numberButton4 = findViewById(R.id.numberButton4);
        Button numberButton5 = findViewById(R.id.numberButton5);
        Button numberButton6 = findViewById(R.id.numberButton6);
        Button numberButton7 = findViewById(R.id.numberButton7);
        Button numberButton8 = findViewById(R.id.numberButton8);
        Button numberButton9 = findViewById(R.id.numberButton9);

        // Add listeners to buttons to handle number selection
        numberButton1.setOnClickListener(v -> onNumberSelected(1));
        numberButton2.setOnClickListener(v -> onNumberSelected(2));
        numberButton3.setOnClickListener(v -> onNumberSelected(3));
        numberButton4.setOnClickListener(v -> onNumberSelected(4));
        numberButton5.setOnClickListener(v -> onNumberSelected(5));
        numberButton6.setOnClickListener(v -> onNumberSelected(6));
        numberButton7.setOnClickListener(v -> onNumberSelected(7));
        numberButton8.setOnClickListener(v -> onNumberSelected(8));
        numberButton9.setOnClickListener(v -> onNumberSelected(9));
    }

    private void onNumberSelected(int number) {
        // Handle number selection logic
        if (selectedCell != null) {
            // Insert the number in the selected cell
            // TODO: Update the cell with the selected number
            checkIfGameIsSolved();
        }
    }

    private void checkIfGameIsSolved() {
        if (game.isSolved()) {
            endGame(true); // End the game if solved
        }
    }

    public void endGame(boolean isSolved) {
        isGameOver = true;
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Stop the timer if the game ends
        }

        // Display the appropriate message based on game outcome
        if (isSolved) {
            displayMessage("Congratulations! You solved the puzzle!");
        } else {
            displayMessage("Time's up! Try again next time.");
        }

        // Show the dashboard button
        dashboardButton.setVisibility(View.VISIBLE);
    }

    public void displayMessage(String message) {
        messageTextView.setText(message);
    }

    public void redirect_player_dashboard_activity() {
        // Logic to go to the dashboard
        Intent intent = new Intent(this, PlayerDashboardActivity.class);
        startActivity(intent);
    }
}
