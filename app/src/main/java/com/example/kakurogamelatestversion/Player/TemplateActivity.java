package com.example.kakurogamelatestversion.Player;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.Game.Template;
import com.example.kakurogamelatestversion.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class TemplateActivity extends AppCompatActivity {

    private TextView templateTitleTextView;
    private String templateUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);  // This is the layout where you display the template details

        templateTitleTextView = findViewById(R.id.templateTitleTextView);

        // Get the template UID passed from EasyLevelActivity
        templateUid = getIntent().getStringExtra("templateUid");

        // Load the template data from Firestore
        loadTemplate();
    }

    private void loadTemplate() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("templates").document(templateUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Template template = documentSnapshot.toObject(Template.class);
                        if (template != null) {
                            // Display the template details, like the grid and clues
                            templateTitleTextView.setText("Template: " + template.getGridUid());
                            // You can populate more views for grid data here
                        }
                    }
                });
    }
}
