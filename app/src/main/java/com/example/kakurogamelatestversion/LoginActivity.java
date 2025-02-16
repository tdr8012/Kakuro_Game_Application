package com.example.kakurogamelatestversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser player = mAuth.getCurrentUser();
                        if (player != null) {
                            fetchPlayerData(player.getUid());
                        }
                    } else {
                        Toast.makeText(this, "Login failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                    }
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
                            Toast.makeText(this, "Welcome " + player.firstName, Toast.LENGTH_SHORT).show();
                            navigateToPlayerDashboardActivity();
                        }
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                });
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
