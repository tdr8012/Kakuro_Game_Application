package com.example.kakurogamelatestversion.Player;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kakurogamelatestversion.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtEmail, txtPhone, txtScore, txtRole, txtUid, txtCreated;
    private ImageView imageProfile;
    private Button goBackBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtScore = findViewById(R.id.txtScore);
        txtRole = findViewById(R.id.txtRole);
        txtUid = findViewById(R.id.txtUid);
        txtCreated = findViewById(R.id.txtCreatedAt);
        imageProfile = findViewById(R.id.imageProfile);
        goBackBtn = findViewById(R.id.goBackBtn);

        goBackBtn.setOnClickListener(v -> finish());

        validateAccessAndLoadProfile();
    }

    private void validateAccessAndLoadProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || user.isAnonymous()) {
            Toast.makeText(this, "Profile not available for guest users", Toast.LENGTH_SHORT).show();
            finish(); // Go back immediately
        } else {
            loadUserProfile(user.getUid());
        }
    }

    private void loadUserProfile(String uid) {
        db.collection("Player").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String firstName = documentSnapshot.getString("firstName");
                String lastName = documentSnapshot.getString("lastName");
                String email = documentSnapshot.getString("email");
                String phone = documentSnapshot.getString("phoneNumber");
                String imgUrl = documentSnapshot.getString("imgUrl");
                String role = documentSnapshot.getString("role");
                Timestamp timestamp = documentSnapshot.getTimestamp("createdAt");
                int score = documentSnapshot.getLong("score") != null ? documentSnapshot.getLong("score").intValue() : 0;

                String createdDate = timestamp != null
                        ? new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(timestamp.toDate())
                        : "N/A";

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
            } else {
                showMissingProfileMessage();
            }
        }).addOnFailureListener(e -> showMissingProfileMessage());
    }

    private void showMissingProfileMessage() {
        Toast.makeText(this, "Unable to load profile. Try again later.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
