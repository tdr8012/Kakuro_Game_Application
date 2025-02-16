package com.example.kakurogamelatestversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailInput, passwordInput, firstNameInput, lastNameInput, phoneNumberInput, confirmPasswordInput;
    private Button registerButton;
    private TextView loginLink, passwordStrengthText;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI Elements
        emailInput = findViewById(R.id.emailRegistration);
        passwordInput = findViewById(R.id.passwordRegistration);
        confirmPasswordInput = findViewById(R.id.confirmPasswordRegistration);
        firstNameInput = findViewById(R.id.firstName);
        lastNameInput = findViewById(R.id.lastName);
        phoneNumberInput = findViewById(R.id.phoneNumber);
        registerButton = findViewById(R.id.registerBtn);
        loginLink = findViewById(R.id.loginLink);
        passwordStrengthText = findViewById(R.id.passwordStrengthText);

        if (registerButton == null) {
            Log.e("RegisterActivity", "registerBtn is NULL. Check activity_register.xml for correct ID.");
            return;
        }

        // Show Password Strength While Typing
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordStrengthText.setVisibility(View.VISIBLE);
                passwordStrengthText.setText(checkPasswordStrength(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // ✅ Handle Clicks
        registerButton.setOnClickListener(view -> registerPlayer());
        loginLink.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private String checkPasswordStrength(String password) {
        if (password.length() < 6) return "Weak";
        else if (password.length() < 10) return "Medium";
        else return "Strong";
    }

    private void registerPlayer() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String phoneNumber = phoneNumberInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser player = mAuth.getCurrentUser();
                        if (player != null) {
                            savePlayerToFirestore(player.getUid(), firstName, lastName, email, phoneNumber, password);
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void savePlayerToFirestore(String uid, String firstName, String lastName, String email, String phoneNumber, String password) {
        db.collection("Player")
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // Initialize collection by adding a placeholder document
                        db.collection("Player").document("init")
                                .set(new HashMap<String, Object>())
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Player collection initialized"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Failed to initialize Player collection", e));
                    }

                    // Now proceed to save the actual player data
                    Map<String, Object> player = new HashMap<>();
                    player.put("uid", uid);
                    player.put("firstName", firstName);
                    player.put("lastName", lastName);
                    player.put("email", email);
                    player.put("phoneNumber", phoneNumber);
                    player.put("password", password);
                    player.put("createdAt", Timestamp.now());
                    player.put("imgUrl", "");
                    player.put("score", 0);
                    player.put("role", "player");

                    db.collection("Player").document(uid)
                            .set(player)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, PlayerDashboardActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error saving user data", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error checking Player collection", e));
    }

}
