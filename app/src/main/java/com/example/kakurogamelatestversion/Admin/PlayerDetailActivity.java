package com.example.kakurogamelatestversion.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kakurogamelatestversion.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlayerDetailActivity extends AppCompatActivity {

    private TextView txtName, txtEmail, txtPhone, txtScore, txtRole, txtUid, txtCreated;
    private ImageView imageProfile;
    private Button goBackBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtScore = findViewById(R.id.txtScore);
        txtRole = findViewById(R.id.txtRole);
        txtUid = findViewById(R.id.txtUid);
        txtCreated = findViewById(R.id.txtCreatedAt);
        imageProfile = findViewById(R.id.imageProfile);
        goBackBtn = findViewById(R.id.goBackBtn);

        // Receive intent extras
        String uid = getIntent().getStringExtra("uid");
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phoneNumber");
        String imgUrl = getIntent().getStringExtra("imgUrl");
        int score = getIntent().getIntExtra("score", 0);
        String role = getIntent().getStringExtra("role");
        long createdMillis = getIntent().getLongExtra("createdAt", -1L);

        String createdDate = createdMillis != -1
                ? new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(createdMillis))
                : "N/A";

        // Set UI
        txtName.setText(firstName + " " + lastName);
        txtEmail.setText(email);
        txtPhone.setText(phone);
        txtScore.setText("Score: " + score);
        txtRole.setText("Role: " + role);
        txtUid.setText("UID: " + uid);
        txtCreated.setText("Joined: " + createdDate);

        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(this).load(imgUrl).circleCrop().into(imageProfile);
        } else {
            imageProfile.setImageResource(R.drawable.ic_profile_placeholder);
        }

        // Back Button
        goBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerDetailActivity.this, ViewPlayersActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // optionally clear stack
            startActivity(intent);
            finish();
        });
    }
}
