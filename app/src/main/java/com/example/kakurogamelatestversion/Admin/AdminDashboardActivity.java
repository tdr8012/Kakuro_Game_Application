package com.example.kakurogamelatestversion.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.R;
import com.example.kakurogamelatestversion.Utils.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
        Button addGameTemplateBtn = findViewById(R.id.addGameTemplateBtn);
        Button adminLogoutBtn = findViewById(R.id.adminLogoutBtn);

        viewPlayersBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ViewPlayersActivity.class)));

        // You can enable this when needed
        // addGameTemplateBtn.setOnClickListener(v ->
        //         startActivity(new Intent(this, AddGameTemplateActivity.class)));

        adminLogoutBtn.setOnClickListener(v -> logoutAdmin());

        // 🔁 Seed button triggers template upload

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
}
