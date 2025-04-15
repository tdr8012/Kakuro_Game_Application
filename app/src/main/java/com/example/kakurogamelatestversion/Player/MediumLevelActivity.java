package com.example.kakurogamelatestversion.Player;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.Game.Template;
import com.example.kakurogamelatestversion.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MediumLevelActivity extends AppCompatActivity {

    private LinearLayout levelButtonsLayout;
    private TextView levelTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        levelButtonsLayout = findViewById(R.id.levelButtonsLayout);
        levelTitle = findViewById(R.id.levelTitle);
        levelTitle.setText("Medium Level Templates");

        loadTemplates("medium");
    }

    private void loadTemplates(String difficulty) {
        FirebaseFirestore.getInstance().collection("templates")
                .whereEqualTo("difficulty", difficulty)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Template> templates = queryDocumentSnapshots.toObjects(Template.class);

                    for (int i = 0; i < templates.size(); i++) {
                        Template template = templates.get(i);
                        Button button = new Button(this);
                        button.setText("Level " + (i + 1));
                        button.setOnClickListener(v -> {
                            Intent intent = new Intent(MediumLevelActivity.this, TemplateActivity.class);
                            intent.putExtra("templateUid", template.getGridUid());
                            startActivity(intent);
                        });
                        levelButtonsLayout.addView(button);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading templates.", Toast.LENGTH_SHORT).show());
    }
}
