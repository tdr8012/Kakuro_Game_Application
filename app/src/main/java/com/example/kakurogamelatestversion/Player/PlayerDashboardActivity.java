package com.example.kakurogamelatestversion.Player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.R;
import com.example.kakurogamelatestversion.Utils.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class PlayerDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_dashboard);

        mAuth = FirebaseAuth.getInstance();

        Button btnEasy = findViewById(R.id.easyBtn);
        Button btnMedium = findViewById(R.id.mediumBtn);
        Button btnHard = findViewById(R.id.hardBtn);
        Button btnLogout = findViewById(R.id.logoutBtn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button gameRuleBtn = findViewById(R.id.gameRuleBtn);
        Button profileBtn = findViewById(R.id.profileBtn);
        Button scoreboardBtn = findViewById(R.id.scoreboardBtn);

        btnEasy.setOnClickListener(v -> startLevelActivity("easy"));
        btnMedium.setOnClickListener(v -> startLevelActivity("medium"));
        btnHard.setOnClickListener(v -> startLevelActivity("hard"));

        btnLogout.setOnClickListener(v -> logoutUser());

        gameRuleBtn.setOnClickListener(v -> navigateToGameRuleActivity());


        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        scoreboardBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerDashboardActivity.this, ScoreboardActivity.class);
            startActivity(intent);
        });

    }
    private void startLevelActivity(String difficulty) {
        Intent intent;
        switch (difficulty) {
            case "easy":
                intent = new Intent(PlayerDashboardActivity.this, EasyLevelActivity.class);
                break;
            case "medium":
                intent = new Intent(PlayerDashboardActivity.this, MediumLevelActivity.class);
                break;
            case "hard":
                intent = new Intent(PlayerDashboardActivity.this, HardLevelActivity.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }
        intent.putExtra("DIFFICULTY_LEVEL", difficulty);
        startActivity(intent);
    }
    private void navigateToGameTimerChallengeActivity() {
        Intent intent = new Intent(PlayerDashboardActivity.this, GameTimerChallengeActivity.class);
        startActivity(intent);
    }

    private void navigateToGameRuleActivity() {
        Intent intent = new Intent(PlayerDashboardActivity.this, GameRuleActivity.class);
        startActivity(intent);
        finish();
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}