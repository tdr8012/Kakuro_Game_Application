package com.example.kakurogamelatestversion.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ResetPlayerActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uid = getIntent().getStringExtra("uid");

        db = FirebaseFirestore.getInstance();

        if (uid != null) {
            resetPlayer(uid);
        } else {
            noPlayerMessage("Invalid Player ID");
        }
    }

    private void resetPlayer(String uid) {
        DocumentReference playerRef = db.collection("Player").document(uid);

        playerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                setScoreToZero(playerRef);

                setProgressToZero(playerRef);
            } else {
                noPlayerMessage("Player not found");
            }
        }).addOnFailureListener(e -> {
            noPlayerMessage("Failed to fetch player");
        });
    }

    private void setScoreToZero(DocumentReference playerRef) {
        playerRef.update("score", 0)
                .addOnSuccessListener(unused -> returnUpdatedValues("Score reset to 0"))
                .addOnFailureListener(e -> noPlayerMessage("Failed to reset score"));
    }

    private void setProgressToZero(DocumentReference playerRef) {
        playerRef.update("progress", 0)
                .addOnSuccessListener(unused -> returnUpdatedValues("Progress reset to 0"))
                .addOnFailureListener(e -> noPlayerMessage("Failed to reset progress"));
    }

    private void returnUpdatedValues(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void noPlayerMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }
}
