package com.example.kakurogamelatestversion.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kakurogamelatestversion.Adapter.PlayerAdapter;
import com.example.kakurogamelatestversion.Models.Player;
import com.example.kakurogamelatestversion.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewPlayersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private FirebaseFirestore db;
    private View progressBar;
    private Button goBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_players);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        goBackBtn = findViewById(R.id.goBackBtn); // 🔹 this was missing
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Player")
                .orderBy("score", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(query, Player.class)
                .build();

        adapter = new PlayerAdapter(options, player -> {
            Intent intent = new Intent(ViewPlayersActivity.this, PlayerDetailActivity.class);
            intent.putExtra("uid", player.getUid());
            intent.putExtra("firstName", player.getFirstName());
            intent.putExtra("lastName", player.getLastName());
            intent.putExtra("email", player.getEmail());
            intent.putExtra("phoneNumber", player.getPhoneNumber());
            intent.putExtra("imgUrl", player.getImgUrl());
            intent.putExtra("score", player.getScore() != null ? player.getScore() : 0);
            intent.putExtra("role", player.getRole());
            intent.putExtra("createdAt", player.getCreatedAt() != null ? player.getCreatedAt().toDate().getTime() : -1L);
            startActivity(intent);
        });

        goBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ViewPlayersActivity.this, AdminDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }
}
