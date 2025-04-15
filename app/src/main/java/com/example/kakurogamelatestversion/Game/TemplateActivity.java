package com.example.kakurogamelatestversion.Game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.Player.PlayerDashboardActivity;
import com.example.kakurogamelatestversion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TemplateActivity extends AppCompatActivity {

    private TextView templateTitleTextView, hintTextView, timerTextView;
    private GridLayout gridContainer;
    private Button playAgainBtn;
    private String difficulty;
    private Template selectedTemplate;
    private CountDownTimer countDownTimer;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Game game;


    private int remainingHints = 0;
    private boolean gameStateOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        templateTitleTextView = findViewById(R.id.templateTitleTextView);
        hintTextView = findViewById(R.id.hintTextView);
        timerTextView = findViewById(R.id.timerTextView);
        gridContainer = findViewById(R.id.gridContainer);

        Button stopGameBtn = findViewById(R.id.stopGameBtn);
        Button hintBtn = findViewById(R.id.hintBtn);
        playAgainBtn = findViewById(R.id.playAgainBtn);

        Button checkBtn = findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(v -> checkGameCompletion());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        difficulty = getIntent().getStringExtra("difficulty");
        if (difficulty == null || difficulty.trim().isEmpty()) {
            Toast.makeText(this, "No difficulty passed! Using default: easy", Toast.LENGTH_SHORT).show();
            difficulty = "easy";
        }

        createGame();
        setGameStateOn();
        game = new Game();
        setLevelDifficulty(difficulty);
        loadTemplates();

        stopGameBtn.setOnClickListener(v -> stopGame());

        playAgainBtn.setVisibility(View.GONE);
        playAgainBtn.setOnClickListener(v -> {
            createGame();
            setLevelDifficulty(difficulty);
            loadTemplates();
            playAgainBtn.setVisibility(View.GONE);
        });

        if (difficulty.equals("easy") || difficulty.equals("medium")) {
            hintBtn.setVisibility(View.VISIBLE);
            hintBtn.setOnClickListener(v -> showHint());
        } else {
            hintBtn.setVisibility(View.GONE);
        }
    }

    private void createGame() {
        selectedTemplate = new Template();
        selectedTemplate.cellStates = new ArrayList<>();
    }

    private void setGameStateOn() {
        gameStateOn = true;
    }


    private void setLevelDifficulty(String difficultyLevel) {
        if (difficultyLevel.equals("easy")) {
            game.setHintOn(Integer.MAX_VALUE);
            hintTextView.setText("Hint: Unlimited");
            timerTextView.setText("Timer: Off");
        } else if (difficultyLevel.equals("medium")) {
            game.setHintOn(3);
            hintTextView.setText("Hint: 3");
            game.setTimerOn(this, timerTextView, this::endGame);
        } else {
            game.setHintOff();
            hintTextView.setText("Hint: Disabled");
            game.setTimerOn(this, timerTextView, this::endGame);
        }
    }


    private void setTimerOn() {
        countDownTimer = new CountDownTimer(5 * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                timerTextView.setText("Timer: " + minutes + ":" + String.format("%02d", remainingSeconds));
            }

            public void onFinish() {
                Toast.makeText(TemplateActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                endGame();
            }
        };
        countDownTimer.start();
    }

    private void loadTemplates() {
        int size = difficulty.equals("easy") ? 3 : (difficulty.equals("medium") ? 4 : 5);
        List<String[][]> templates = loadTemplates(difficulty);
        List<List<Integer>> solutions = getSolutions(difficulty);

        if (templates == null || solutions == null || templates.isEmpty() || solutions.isEmpty()) {
            Toast.makeText(this, "No templates or solutions available for difficulty: " + difficulty, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int index = new Random().nextInt(Math.min(templates.size(), solutions.size()));

        String[][] chosen = templates.get(index);
        List<Integer> flatSolution = solutions.get(index);

        if (flatSolution.size() != size * size) {
            Toast.makeText(this, "Invalid solution size for template!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        selectedTemplate.solution = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            selectedTemplate.solution.add(flatSolution.subList(i * size, (i + 1) * size));
        }

        selectedTemplate.size = size;
        selectedTemplate.grid = new ArrayList<>();
        for (String[] row : chosen) selectedTemplate.grid.add(Arrays.asList(row));
        for (int i = 0; i < size * size; i++) selectedTemplate.cellStates.add(new Template.Cell());
        selectedTemplate.extractHintsFromGrid();

        templateTitleTextView.setText("Template loaded: " + difficulty);
        renderGrid();
    }

    private void showHint() {
        if (difficulty.equals("hard")) return;
        if (!game.hasHints()) {
            Toast.makeText(this, "No hints left!", Toast.LENGTH_SHORT).show();
            return;
        }

        int size = selectedTemplate.size;
        List<Integer> flatSolution = flattenSolution(selectedTemplate.solution);
        for (int i = 0; i < size * size; i++) {
            if (selectedTemplate.cellStates.get(i).getValue() == 0 && flatSolution.get(i) > 0) {
                int val = flatSolution.get(i);
                View cell = gridContainer.getChildAt(i);
                if (cell instanceof EditText) {
                    ((EditText) cell).setText(String.valueOf(val));
                    selectedTemplate.cellStates.get(i).setValue(val);

                    if (difficulty.equals("medium")) {
                        game.consumeHint();
                        updateHintText();
                    }

                    checkGameCompletion();
                    Toast.makeText(this, "Hint filled: " + val, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Toast.makeText(this, "No hint available", Toast.LENGTH_SHORT).show();
    }


    private List<String[][]> loadTemplates(String difficulty) {
        List<String[][]> templates = new ArrayList<>();

        if (difficulty.equals("easy")) {
            templates.add(new String[][]{
                    {"/", "⬍4", "⬍3"},
                    {"➞7", "", ""},
                    {"/", "/", "/"}
            });

            templates.add(new String[][]{
                    {"/", "⬍1", "⬍4"},
                    {"➞5", "", ""},
                    {"/", "/", "/"}
            });

            templates.add(new String[][]{
                    {"/", "⬍5", "⬍4"},
                    {"➞9", "", ""},
                    {"/", "/", "/"}
            });

        } else if (difficulty.equals("medium")) {
            templates.add(new String[][]{
                    {"/", "⬍7", "⬍4", "/"},
                    {"➞10", "", "", "⬍4"},
                    {"/", "/", "➞3", ""},
                    {"/", "/", "/", "/"}
            });

            templates.add(new String[][]{
                    {"/", "⬍6", "⬍8", "/"},
                    {"➞11", "", "", "⬍4"},
                    {"/", "/", "➞7", ""},
                    {"/", "/", "/", "/"}
            });

            templates.add(new String[][]{
                    {"/", "⬍5", "⬍7", "/"},
                    {"➞9", "", "", "⬍4"},
                    {"/", "/", "➞3", ""},
                    {"/", "/", "/", "/"}
            });

        } else if (difficulty.equals("hard")) {
            templates.add(new String[][]{
                    {"/", "⬍8", "⬍9", "⬍7", "/"},
                    {"➞15", "", "", "", "⬍6"},
                    {"➞10", "", "", "", "/"},
                    {"/", "/", "/", "/", "/"},
                    {"/", "/", "/", "/", "/"}
            });

            templates.add(new String[][]{
                    {"/", "⬍6", "⬍11", "⬍7", "/"},
                    {"➞13", "", "", "", "⬍5"},
                    {"➞8", "", "", "", "/"},
                    {"/", "/", "/", "/", "/"},
                    {"/", "/", "/", "/", "/"}
            });

            templates.add(new String[][]{
                    {"/", "⬍7", "⬍10", "⬍8", "/"},
                    {"➞14", "", "", "", "⬍7"},
                    {"➞9", "", "", "", "/"},
                    {"/", "/", "/", "/", "/"},
                    {"/", "/", "/", "/", "/"}
            });
        }

        return templates;
    }

    private List<List<Integer>> getSolutions(String difficulty) {
        List<List<Integer>> solutions = new ArrayList<>();

        if (difficulty.equals("easy")) {
            solutions.add(Arrays.asList(0, 0, 0, 3, 4, 0, 0, 0, 0)); // 3+4 = 7
            solutions.add(Arrays.asList(0, 0, 0, 2, 3, 0, 0, 0, 0)); // 2+3 = 5
            solutions.add(Arrays.asList(0, 0, 0, 4, 5, 0, 0, 0, 0)); // 4+5 = 9

        } else if (difficulty.equals("medium")) {
            solutions.add(Arrays.asList(
                    0, 0, 0, 0,
                    6, 4, 0, 4,
                    0, 0, 1, 2,
                    0, 0, 0, 0
            ));

            solutions.add(Arrays.asList(
                    0, 0, 0, 0,
                    5, 6, 0, 4,
                    0, 0, 3, 4,
                    0, 0, 0, 0
            ));

            solutions.add(Arrays.asList(
                    0, 0, 0, 0,
                    2, 7, 0, 4,
                    0, 0, 1, 2,
                    0, 0, 0, 0
            ));

        } else if (difficulty.equals("hard")) {
            solutions.add(Arrays.asList(
                    0, 0, 0, 0, 0,
                    4, 5, 3, 3, 0,
                    3, 2, 4, 1, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0
            ));

            solutions.add(Arrays.asList(
                    0, 0, 0, 0, 0,
                    5, 6, 2, 0, 0,
                    3, 4, 1, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0
            ));

            solutions.add(Arrays.asList(
                    0, 0, 0, 0, 0,
                    6, 5, 2, 1, 0,
                    3, 2, 4, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0
            ));
        }

        return solutions;
    }


    private void updateHintText() {
        if (difficulty.equals("medium")) {
            hintTextView.setText("Hint: " + game.getRemainingHints());
        }
    }


    private List<Integer> flattenSolution(List<List<Integer>> solution) {
        List<Integer> flat = new ArrayList<>();
        for (List<Integer> row : solution) flat.addAll(row);
        return flat;
    }

    private void checkGameCompletion() {
        int size = selectedTemplate.size;
        boolean isComplete = true;

        List<Integer> flatSolution = flattenSolution(selectedTemplate.solution);
        int solutionIndex = 0;

        for (int i = 0; i < gridContainer.getChildCount(); i++) {
            View cell = gridContainer.getChildAt(i);

            if (cell instanceof EditText) {
                EditText input = (EditText) cell;
                String valStr = input.getText().toString().trim();
                int expected = flatSolution.get(solutionIndex);
                int entered = valStr.isEmpty() ? 0 : Integer.parseInt(valStr);

                if (entered != expected) {
                    isComplete = false;
                    break;
                }
                solutionIndex++;
            } else if (cell instanceof TextView) {
                if (solutionIndex < flatSolution.size()) {
                    solutionIndex++;
                }
            }
        }

        if (isComplete) {
            Toast.makeText(this, "✅ Correct! You solved the puzzle!", Toast.LENGTH_LONG).show();
            playAgainBtn.setVisibility(View.VISIBLE);
            updateScore();
        } else {
            Toast.makeText(this, "❌ Incorrect, try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateScore() {
        int scoreToAdd = difficulty.equals("easy") ? 1 : difficulty.equals("medium") ? 2 : 3;
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (uid == null) return;

        db.collection("Player").document(uid).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                long currentScore = snapshot.getLong("score") != null ? snapshot.getLong("score") : 0;
                db.collection("Player").document(uid).update("score", currentScore + scoreToAdd);
            }
        });
    }

    private void renderGrid() {
        int size = selectedTemplate.size;
        gridContainer.removeAllViews();
        gridContainer.setColumnCount(size);
        gridContainer.setRowCount(size);

        for (int row = 0; row < selectedTemplate.grid.size(); row++) {
            for (int col = 0; col < selectedTemplate.grid.get(row).size(); col++) {
                String cell = selectedTemplate.grid.get(row).get(col);
                int index = row * size + col;

                if (cell.equals("")) {
                    EditText input = new EditText(this);
                    input.setGravity(Gravity.CENTER);
                    input.setTextColor(getResources().getColor(android.R.color.white));
                    input.setTextSize(20);
                    input.setBackgroundResource(R.drawable.cell_border);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 180;
                    params.height = 180;
                    params.setMargins(4, 4, 4, 4);
                    input.setLayoutParams(params);
                    input.setId(View.generateViewId());
                    int finalIndex = index;
                    input.setOnFocusChangeListener((v, hasFocus) -> {
                        if (!hasFocus) {
                            String val = input.getText().toString();
                            int value = val.isEmpty() ? 0 : Integer.parseInt(val);
                            selectedTemplate.cellStates.get(finalIndex).setValue(value);
                            checkGameCompletion();
                        }
                    });
                    gridContainer.addView(input);
                } else {
                    TextView cellView = new TextView(this);
                    cellView.setText(cell);
                    cellView.setGravity(Gravity.CENTER);
                    cellView.setBackgroundResource(R.drawable.cell_border);
                    cellView.setTextColor(getResources().getColor(android.R.color.white));
                    cellView.setTextSize(20);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 180;
                    params.height = 180;
                    params.setMargins(4, 4, 4, 4);
                    cellView.setLayoutParams(params);
                    gridContainer.addView(cellView);
                }
            }
        }
    }

    private void stopGame() {
        game.setTimerOff();
        gameStateOn = false;
        Toast.makeText(this, "Game stopped", Toast.LENGTH_SHORT).show();
        redirectToDashboard();
    }

    @Override
    protected void onDestroy() {
        game.setTimerOff();
        super.onDestroy();
    }


    private void endGame() {
        gameStateOn = false;
        Toast.makeText(this, "Game ended", Toast.LENGTH_SHORT).show();
        redirectToDashboard();
    }

    private void redirectToDashboard() {
        Intent intent = new Intent(TemplateActivity.this, PlayerDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
