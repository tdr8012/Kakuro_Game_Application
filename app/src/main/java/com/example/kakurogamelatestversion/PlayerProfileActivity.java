package com.example.kakurogamelatestversion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;

    private TextView playerNameTextView;
    private TextView playerEmailTextView;
    private TextView playerScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        playerNameTextView = findViewById(R.id.playerNameTextView);
        playerEmailTextView = findViewById(R.id.playerEmailTextView);
        playerScoreTextView = findViewById(R.id.playerScoreTextView);
        findViewById(R.id.profileImageView);

        Button returnToDashboardButton = findViewById(R.id.returnToDashboardButton);
        returnToDashboardButton.setOnClickListener(v -> navigateToPlayerDashboardActivity());

            // Fetch player data from Firestore if the user is logged in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                userId = user.getUid();
                fetchPlayerData();
            } else {
                // Handle case if no user is logged in
                Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
                finish();
            }

    }


    // Fetch the player data from Firestore
    private void fetchPlayerData() {
        db.collection("players").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Player player = documentSnapshot.toObject(Player.class);
                        if (player != null) {
                            // Log the player data to see if it's being fetched correctly
                            Log.d("PlayerProfile", "Player Data: " + player.getFirstName() + " " + player.getLastName() + " " + player.getEmail() + " Score: " + player.getScore());

                            // Now set the player data to the views
                            playerNameTextView.setText(player.getFirstName() + " " + player.getLastName());
                            playerEmailTextView.setText(player.getEmail());
                            playerScoreTextView.setText("Score: " + player.getScore());
                        } else {
                            Log.e("PlayerProfile", "Player data is null.");
                        }
                    } else {
                        Log.e("PlayerProfile", "Player document does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PlayerProfile", "Error fetching player data: ", e);
                    Toast.makeText(PlayerProfileActivity.this, "Error fetching player data", Toast.LENGTH_SHORT).show();
                });
    }
    private void navigateToPlayerDashboardActivity() {
        Intent intent = new Intent(PlayerProfileActivity.this, PlayerDashboardActivity.class);
        startActivity(intent);
        finish();
    }
}

