package com.example.kakurogamelatestversion.Player;

import android.content.Intent;
import android.graphics.Color;
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

import com.example.kakurogamelatestversion.Game.ChallengeTemplate;
import com.example.kakurogamelatestversion.Game.Template;
import com.example.kakurogamelatestversion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GameChallengeActivity extends AppCompatActivity {

    private TextView timerTextView, scoreTextView;
    private GridLayout gridContainer;
    private Button checkBtn, endBtn;

    private CountDownTimer challengeTimer;
    private int score = 0;
    private long timeLeft = 6 * 60 * 1000;

    private List<String[][]> templateGrids;
    private List<List<Integer>> templateSolutions;
    private int currentTemplateIndex = -1;

    private final int SIZE = 3;
    private Template currentTemplate;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private List<EditText> editableCells = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_challenge);

        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        gridContainer = findViewById(R.id.gridContainer);
        checkBtn = findViewById(R.id.completeButton);
        endBtn = findViewById(R.id.endButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        startChallengeMode();

        Button skipBtn = findViewById(R.id.skipButton);
        skipBtn.setOnClickListener(v -> loadTemplate());


        checkBtn.setOnClickListener(v -> checkChallengeCompletion());
        endBtn.setOnClickListener(v -> endChallengeMode());
    }

    private void startChallengeMode() {
        currentTemplate = new Template();
        currentTemplate.cellStates = new ArrayList<>();

        templateGrids = ChallengeTemplate.getTemplates();
        templateSolutions = ChallengeTemplate.getSolutions();

        startTimer();
        loadTemplate();
    }

    private void startTimer() {
        challengeTimer = new CountDownTimer(timeLeft, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                int min = (int) (millisUntilFinished / 60000);
                int sec = (int) ((millisUntilFinished % 60000) / 1000);
                timerTextView.setText(String.format("%02d:%02d", min, sec));
            }

            public void onFinish() {
                endChallengeMode();
            }
        }.start();
    }

    private void loadTemplate() {
        editableCells.clear();
        gridContainer.removeAllViews();
        gridContainer.setColumnCount(SIZE);
        gridContainer.setRowCount(SIZE);

        currentTemplateIndex = (currentTemplateIndex + 1) % templateGrids.size();

        // Set grid
        currentTemplate.grid = new ArrayList<>();
        for (String[] row : templateGrids.get(currentTemplateIndex)) {
            currentTemplate.grid.add(List.of(row));
        }

        // Set solution
        currentTemplate.solution = new ArrayList<>();
        List<Integer> flat = templateSolutions.get(currentTemplateIndex);
        for (int i = 0; i < SIZE; i++) {
            currentTemplate.solution.add(flat.subList(i * SIZE, (i + 1) * SIZE));
        }

        // Render
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String cell = currentTemplate.grid.get(row).get(col);

                if (cell.equals("")) {
                    EditText input = new EditText(this);
                    input.setTextColor(Color.WHITE);
                    input.setGravity(Gravity.CENTER);
                    input.setTextSize(20);
                    input.setBackgroundResource(R.drawable.cell_border);

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 180;
                    params.height = 180;
                    params.setMargins(4, 4, 4, 4);
                    input.setLayoutParams(params);

                    editableCells.add(input);
                    gridContainer.addView(input);
                } else {
                    TextView cellView = new TextView(this);
                    cellView.setText(cell);
                    cellView.setTextColor(Color.WHITE);
                    cellView.setGravity(Gravity.CENTER);
                    cellView.setTextSize(20);
                    cellView.setBackgroundResource(R.drawable.cell_border);

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

    private void checkChallengeCompletion() {
        List<Integer> flatSolution = flattenSolution();
        boolean correct = true;

        int flatIndex = 0;
        int editableIndex = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String cell = currentTemplate.grid.get(row).get(col);

                if (cell.equals("")) {
                    EditText input = editableCells.get(editableIndex++);
                    String val = input.getText().toString().trim();
                    int entered = val.isEmpty() ? 0 : Integer.parseInt(val);
                    int expected = flatSolution.get(flatIndex);

                    if (entered != expected) {
                        correct = false;
                        break;
                    }

                    flatIndex++;
                } else {
                    flatIndex++;
                }
            }
        }

        if (correct) {
            Toast.makeText(this, "✅ Correct!", Toast.LENGTH_SHORT).show();
            updateChallengeScore();
            loadTemplate();
        } else {
            Toast.makeText(this, "❌ Try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private List<Integer> flattenSolution() {
        List<Integer> flat = new ArrayList<>();
        for (List<Integer> row : currentTemplate.solution) {
            flat.addAll(row);
        }
        return flat;
    }

    private void updateChallengeScore() {
        score++;
        scoreTextView.setText("Score: " + score);
    }

    private void endChallengeMode() {
        if (challengeTimer != null) challengeTimer.cancel();

        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid != null) {
            db.collection("Player").document(uid)
                    .update("challengeScore", score)
                    .addOnSuccessListener(aVoid -> {})
                    .addOnFailureListener(e -> {});
        }

        Toast.makeText(this, "Challenge over! Final Score: " + score, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, PlayerDashboardActivity.class));
        finish();
    }
}
