package com.example.kakurogamelatestversion.Player;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GameTimerChallengeActivity extends AppCompatActivity {

    private TextInputEditText timeLimitInput;
    private TextInputLayout timeLimitInputLayout;
    private MaterialButton saveBtn;
    private View progressBar, formContainer;
    private FirebaseFirestore db;

    private static final int MIN_TIME_LIMIT = 1; // 1 minute minimum
    private static final int MAX_TIME_LIMIT = 30; // 30 minutes maximum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_timer_challenge);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize views
        timeLimitInput = findViewById(R.id.timeLimitInput);
        timeLimitInputLayout = findViewById(R.id.timeLimitInputLayout);
        saveBtn = findViewById(R.id.saveBtn);
        progressBar = findViewById(R.id.progressBar);
        formContainer = findViewById(R.id.formContainer);

        // Configure input
        configureInputField();

        // Load existing settings
        loadCurrentSettings();

        // Set click listener
        saveBtn.setOnClickListener(v -> saveTimeLimit());
    }

    private void configureInputField() {
        timeLimitInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        timeLimitInputLayout.setHint(getString(R.string.time_limit_hint_with_range,
                MIN_TIME_LIMIT, MAX_TIME_LIMIT));
    }

    private void loadCurrentSettings() {
        showProgress(true);

        // Try loading from Firestore first
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId != null) {
            db.collection("user_settings").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Long minutes = documentSnapshot.getLong("timerChallengeMinutes");
                            if (minutes != null) {
                                timeLimitInput.setText(String.valueOf(minutes));
                            }
                        } else {
                            // Fallback to SharedPreferences
                            loadFromSharedPreferences();
                        }
                        showProgress(false);
                    })
                    .addOnFailureListener(e -> {
                        loadFromSharedPreferences();
                        showProgress(false);
                    });
        } else {
            loadFromSharedPreferences();
            showProgress(false);
        }
    }

    private void loadFromSharedPreferences() {
        String savedTime = getSharedPreferences("game_settings", MODE_PRIVATE)
                .getString("time_limit", "5"); // Default to 5 minutes
        timeLimitInput.setText(savedTime);
    }

    private void saveTimeLimit() {
        String timeLimitStr = timeLimitInput.getText().toString().trim();

        if (timeLimitStr.isEmpty()) {
            timeLimitInputLayout.setError(getString(R.string.error_time_required));
            return;
        }

        try {
            int timeLimit = Integer.parseInt(timeLimitStr);

            if (timeLimit < MIN_TIME_LIMIT || timeLimit > MAX_TIME_LIMIT) {
                timeLimitInputLayout.setError(getString(R.string.error_time_range,
                        MIN_TIME_LIMIT, MAX_TIME_LIMIT));
                return;
            }

            // Clear error
            timeLimitInputLayout.setError(null);
            showProgress(true);

            // Save locally
            saveToSharedPreferences(timeLimit);

            // Save to Firestore if user is logged in
            saveToFirestore(timeLimit);

        } catch (NumberFormatException e) {
            timeLimitInputLayout.setError(getString(R.string.error_invalid_number));
        }
    }

    private void saveToSharedPreferences(int minutes) {
        getSharedPreferences("game_settings", MODE_PRIVATE)
                .edit()
                .putString("time_limit", String.valueOf(minutes))
                .apply();
    }

    private void saveToFirestore(int minutes) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) {
            showSuccess();
            return;
        }

        Map<String, Object> settings = new HashMap<>();
        settings.put("timerChallengeMinutes", minutes);
        settings.put("lastUpdated", System.currentTimeMillis());

        db.collection("user_settings").document(userId)
                .set(settings)
                .addOnSuccessListener(aVoid -> showSuccess())
                .addOnFailureListener(e -> showError(e.getMessage()));
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        formContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showSuccess() {
        showProgress(false);
        Toast.makeText(this, R.string.settings_saved_success, Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        showProgress(false);
        Toast.makeText(this, getString(R.string.settings_save_error, message),
                Toast.LENGTH_LONG).show();
    }
}