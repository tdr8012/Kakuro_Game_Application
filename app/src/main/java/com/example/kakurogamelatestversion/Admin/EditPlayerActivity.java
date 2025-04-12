package com.example.kakurogamelatestversion.Admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPlayerActivity extends AppCompatActivity {

    private EditText playerNameInput, playerEmailInput;
    private FirebaseFirestore db;
    private String playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);


        db = FirebaseFirestore.getInstance();


        playerId = getIntent().getStringExtra("PLAYER_ID");
        if (playerId == null || playerId.isEmpty()) {
            Toast.makeText(this, "Player not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        playerNameInput = findViewById(R.id.playerNameInput);
        playerEmailInput = findViewById(R.id.playerEmailInput);
        Button savePlayerBtn = findViewById(R.id.savePlayerBtn);


        loadPlayerData();


        savePlayerBtn.setOnClickListener(v -> savePlayerChanges());
    }

    private void loadPlayerData() {
        db.collection("players").document(playerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");

                        playerNameInput.setText(name);
                        playerEmailInput.setText(email);
                    } else {
                        Toast.makeText(this, "Player data not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading player data", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void savePlayerChanges() {
        String playerName = playerNameInput.getText().toString().trim();
        String playerEmail = playerEmailInput.getText().toString().trim();

        if (playerName.isEmpty() || playerEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        db.collection("players").document(playerId)
                .update(
                        "name", playerName,
                        "email", playerEmail
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Player updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating player: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}