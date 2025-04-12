// DeletePlayerActivity.java
package com.example.kakurogamelatestversion.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kakurogamelatestversion.Adapter.PlayerAdapter;
import com.example.kakurogamelatestversion.Models.Player;
import com.example.kakurogamelatestversion.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DeletePlayerActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private PlayerAdapter adapter;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_player);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        setupRecyclerView();
        setupLogoutButton();
    }

    private void setupRecyclerView() {
        Query query = db.collection("players")
                .orderBy("firstName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(query, Player.class)
                .build();

        adapter = new PlayerAdapter(options, this::showDeleteConfirmationDialog);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void showDeleteConfirmationDialog(Player player) {
        String playerName = player.getFirstName() + " " + player.getLastName();
        new AlertDialog.Builder(this)
                .setTitle("Delete Player")
                .setMessage("Delete " + playerName + " permanently?")
                .setPositiveButton("Delete", (dialog, which) -> deletePlayer(player))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deletePlayer(Player player) {
        showProgress(true);

        db.collection("players").document(player.getUid())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, player.getFirstName() + " " + player.getLastName() + " deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                })
                .addOnCompleteListener(task -> showProgress(false));
    }

    private void setupLogoutButton() {
        findViewById(R.id.logoutBtn).setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        findViewById(R.id.recyclerView).setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}