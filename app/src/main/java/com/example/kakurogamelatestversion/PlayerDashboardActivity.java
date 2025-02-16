package com.example.kakurogamelatestversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button gameRuleBtn = findViewById(R.id.gameRuleBtn);
        
        btnLogout.setOnClickListener(v -> logoutUser());
        gameRuleBtn.setOnClickListener(v -> navigateToGaemRuleActivity());
    }


    private void navigateToGaemRuleActivity() {
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