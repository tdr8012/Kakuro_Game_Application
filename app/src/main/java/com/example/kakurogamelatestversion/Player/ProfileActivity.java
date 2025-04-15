package com.example.kakurogamelatestversion.Player;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kakurogamelatestversion.R;
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
        setContentView(R.layout.activity_player_detail); // Reuse the same layout

        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Init UI
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtScore = findViewById(R.id.txtScore);
        txtRole = findViewById(R.id.txtRole);
        txtUid = findViewById(R.id.txtUid);
        txtCreated = findViewById(R.id.txtCreatedAt);
        imageProfile = findViewById(R.id.imageProfile);
        goBackBtn = findViewById(R.id.goBackBtn);

        // Back Button
        goBackBtn.setOnClickListener(v -> finish());

        // Load user profile
        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            displayGuestMessage();
            return;
        }

        if (user.isAnonymous()) {
            displayGuestMessage();
            return;
        }

        String uid = user.getUid();
        db.collection("Player").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String firstName = documentSnapshot.getString("firstName");
                String lastName = documentSnapshot.getString("lastName");
                String email = documentSnapshot.getString("email");
                String phone = documentSnapshot.getString("phoneNumber");
                String imgUrl = documentSnapshot.getString("imgUrl");
                String role = documentSnapshot.getString("role");
                long createdAt = documentSnapshot.getLong("createdAt") != null ? documentSnapshot.getLong("createdAt") : -1L;
                int score = documentSnapshot.getLong("score") != null ? documentSnapshot.getLong("score").intValue() : 0;

                // Format date
                String createdDate = createdAt != -1
                        ? new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(createdAt))
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
            } else {
                displayGuestMessage(); // fallback
            }
        }).addOnFailureListener(e -> displayGuestMessage());
    }

    private void displayGuestMessage() {
        txtName.setText("Please sign in to view your profile details.");
        txtEmail.setText("");
        txtPhone.setText("");
        txtScore.setText("");
        txtRole.setText("");
        txtUid.setText("");
        txtCreated.setText("");
        imageProfile.setImageResource(R.drawable.ic_profile_placeholder);
    }
}
