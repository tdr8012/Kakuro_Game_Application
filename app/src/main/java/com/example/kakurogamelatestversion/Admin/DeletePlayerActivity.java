package com.example.kakurogamelatestversion.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeletePlayerActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uid = getIntent().getStringExtra("uid");

        db = FirebaseFirestore.getInstance();

        if (uid != null) {
            deletePlayer(uid);
        } else {
            noPlayerMessage("Invalid Player ID");
        }
    }

    private void deletePlayer(String uid) {
        fetchPlayerInfo(uid);
    }

    private void fetchPlayerInfo(String uid) {
        DocumentReference playerRef = db.collection("Player").document(uid);

        playerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                deletePlayerInfo(uid);
            } else {
                noPlayerMessage("Player not found");
            }
        }).addOnFailureListener(e -> {
            noPlayerMessage("Failed to fetch player");
        });
    }

    private void deletePlayerInfo(String uid) {
        db.collection("Player").document(uid)
                .delete() // Step 3.2.2: deleteFromDB()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Player deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    noPlayerMessage("Failed to delete player");
                });
    }

    private void noPlayerMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }
}
