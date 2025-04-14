package com.example.kakurogamelatestversion.Admin;

import android.os.Bundle;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kakurogamelatestversion.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kakurogamelatestversion.R;

public class AddGameTemplateActivity extends AppCompatActivity {
    private EditText templateTitle, templateDifficulty, templateGrid;
    private Button saveTemplateBtn;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_template);
        db = FirebaseFirestore.getInstance();

        templateTitle = findViewById(R.id.templateTitle);
        templateDifficulty = findViewById(R.id.templateDifficulty);
        templateGrid = findViewById(R.id.templateGrid);
        saveTemplateBtn = findViewById(R.id.saveTemplateBtn);

        saveTemplateBtn.setOnClickListener(v -> saveTemplate());
    }

    private void saveTemplate() {
        String title = templateTitle.getText().toString().trim();
        String difficulty = templateDifficulty.getText().toString().trim();
        String grid = templateGrid.getText().toString().trim();

        if (title.isEmpty() || difficulty.isEmpty() || grid.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> template = new HashMap<>();
        template.put("title", title);
        template.put("difficulty", difficulty.toLowerCase());
        template.put("grid", grid);
        template.put("createdAt", System.currentTimeMillis());

        db.collection("gameTemplates")
                .add(template)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Template added!", Toast.LENGTH_SHORT).show();
                    finish(); // go back to admin dashboard
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
