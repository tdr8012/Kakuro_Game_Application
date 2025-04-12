package com.example.kakurogamelatestversion.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.Player.GameTimerChallengeActivity;
import com.example.kakurogamelatestversion.R;
import com.example.kakurogamelatestversion.Utils.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        verifyAdminStatus();


        Button viewPlayersBtn = findViewById(R.id.viewPlayersBtn);
        Button editPlayerBtn = findViewById(R.id.editPlayerBtn);
        Button resetScoreBtn = findViewById(R.id.resetScoreBtn);
        Button resetLevelBtn = findViewById(R.id.resetLevelBtn);
        Button deletePlayerBtn = findViewById(R.id.deletePlayerBtn);
        Button adminLogoutBtn = findViewById(R.id.adminLogoutBtn);
        Button gameTimerChallengeBtn = findViewById(R.id.gameTimerChallengeBtn);


        viewPlayersBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ViewPlayersActivity.class)));

        editPlayerBtn.setOnClickListener(v ->
                startActivity(new Intent(this, EditPlayerActivity.class)));

        resetScoreBtn.setOnClickListener(v ->
                showResetConfirmation("scores", () -> resetAllScores()));

        resetLevelBtn.setOnClickListener(v ->
                showResetConfirmation("levels", () -> resetAllLevels()));

        deletePlayerBtn.setOnClickListener(v ->
                startActivity(new Intent(this, DeletePlayerActivity.class)));

        gameTimerChallengeBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Game Timer Challenge", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GameTimerChallengeActivity.class));
        });

        adminLogoutBtn.setOnClickListener(v -> logoutAdmin());
    }


    private void resetAllScores() {
        db.collection("players")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().update("score", 0)
                                    .addOnFailureListener(e ->
                                            showError("Failed to reset score for: " + document.getId()));
                        }
                        Toast.makeText(this, "All scores reset!", Toast.LENGTH_SHORT).show();
                    } else {
                        showError("Error: " + task.getException().getMessage());
                    }
                });
    }

    private void resetAllLevels() {
        db.collection("players")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().update("levelProgress", 1) // Reset to level 1
                                    .addOnFailureListener(e ->
                                            showError("Failed to reset level for: " + document.getId()));
                        }
                        Toast.makeText(this, "All levels reset!", Toast.LENGTH_SHORT).show();
                    } else {
                        showError("Error: " + task.getException().getMessage());
                    }
                });
    }


    private void verifyAdminStatus() {
        if (mAuth.getCurrentUser() == null) {
            redirectToLogin();
            return;
        }


        db.collection("admins").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || !task.getResult().exists()) {
                        Toast.makeText(this, "Admin access required", Toast.LENGTH_SHORT).show();
                        redirectToLogin();
                    }
                });
    }


    private void showResetConfirmation(String type, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle("Reset Confirmation")
                .setMessage("Are you sure you want to reset all " + type + "?")
                .setPositiveButton("Yes", (dialog, which) -> onConfirm.run())
                .setNegativeButton("No", null)
                .show();
    }

    private void logoutAdmin() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}