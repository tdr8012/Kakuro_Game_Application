package com.example.kakurogamelatestversion.Player;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.Game.TemplateActivity;
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
        Button gameRuleBtn = findViewById(R.id.gameRuleBtn);
        Button profileBtn = findViewById(R.id.profileBtn);
        Button scoreboardBtn = findViewById(R.id.scoreboardBtn);
        Button challengeBtn = findViewById(R.id.challengeBtn);

        btnEasy.setOnClickListener(v -> launchGame("easy"));
        btnMedium.setOnClickListener(v -> launchGame("medium"));
        btnHard.setOnClickListener(v -> launchGame("hard"));

        challengeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameChallengeActivity.class);
            startActivity(intent);
        });


        gameRuleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameRuleActivity.class);
            startActivity(intent);
        });

        profileBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        scoreboardBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ScoreboardActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void launchGame(String difficulty) {
        Intent intent = new Intent(PlayerDashboardActivity.this, TemplateActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}
