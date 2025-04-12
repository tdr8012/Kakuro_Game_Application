package com.example.kakurogamelatestversion.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.Models.Admin;
import com.example.kakurogamelatestversion.Admin.AdminDashboardActivity;
import com.example.kakurogamelatestversion.Models.Player;
import com.example.kakurogamelatestversion.Player.PlayerDashboardActivity;
import com.example.kakurogamelatestversion.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextInputEditText emailInput, passwordInput;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailInput = findViewById(R.id.emailLogin);
        passwordInput = findViewById(R.id.passwordLogin);
        Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(view -> navigateToRegister());
        findViewById(R.id.loginbtn).setOnClickListener(view -> loginUser());
        findViewById(R.id.googleSignInButton).setOnClickListener(view -> playAsGuest());
    }

    private void navigateToRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void loginUser() {
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for fixed admin credentials
        if (email.equals("admin@gmail.com") && password.equals("123456")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                saveAdminToFirestore(user.getUid());
                            }
                        } else {
                            Toast.makeText(this, "Admin login failed. Please register this account first.", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            // Normal user login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserRole(user.getUid());
                            }
                        } else {
                            Toast.makeText(this, "Login failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void saveAdminToFirestore(String uid) {
        db.collection("admins").document(uid).get().addOnSuccessListener(document -> {
            if (!document.exists()) {
                db.collection("admins").document(uid)
                        .set(new Admin(uid, "admin@gmail.com"))
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Admin saved"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error saving admin", e));
            }
            Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
            navigateToAdminDashboardActivity();
        });
    }


    private void fetchPlayerData(String uid) {
        db.collection("Player").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot doc = task.getResult();
                        Player player = doc.toObject(Player.class);

                        if (player != null) {
                            Toast.makeText(this, "Welcome " + player.getFirstName(), Toast.LENGTH_SHORT).show();
                            navigateToPlayerDashboardActivity();
                        }
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkUserRole(String uid) {
        db.collection("admins").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        // User is an admin
                        Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                        navigateToAdminDashboardActivity();
                    } else {
                        // Not an admin, try fetching player data
                        fetchPlayerData(uid);
                    }
                });
    }

    private void navigateToAdminDashboardActivity() {
        Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void playAsGuest() {
        Intent intent = new Intent(this, PlayerDashboardActivity.class);
        intent.putExtra("guest", true);
        startActivity(intent);
        finish();
    }

    private void navigateToPlayerDashboardActivity() {
        Intent intent = new Intent(LoginActivity.this, PlayerDashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
