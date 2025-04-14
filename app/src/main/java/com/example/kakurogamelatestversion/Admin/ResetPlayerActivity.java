package com.example.kakurogamelatestversion.Admin;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.example.kakurogamelatestversion.R;

public class ResetPlayerActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetBtn;
    private FirebaseFirestore db;
    private Button backToAdminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_player);
        emailInput = findViewById(R.id.emailInput);
        resetBtn = findViewById(R.id.resetBtn);
        db = FirebaseFirestore.getInstance();

        resetBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter player email", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("players")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(this, "No player found with that email", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                doc.getReference().update("score", 0)
                                        .addOnSuccessListener(unused ->
                                                Toast.makeText(this, "Score reset for " + email, Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e ->
                                                Toast.makeText(this, "Reset failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });



        backToAdminBtn.setOnClickListener(v -> {
            finish();
        });
    }
}
