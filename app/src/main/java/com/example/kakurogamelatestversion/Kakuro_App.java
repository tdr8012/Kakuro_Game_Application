package com.example.kakurogamelatestversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Kakuro_App extends AppCompatActivity {

    private Game game;
    private Template template;
    private String difficulty;
    FirebaseFirestore db;
    private TextView selectedCell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakuro_app);

        db = FirebaseFirestore.getInstance();

        // Get data passed from Level
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String gridUid = extras.getString("gridUid");
            difficulty = extras.getString("difficulty");
            // Now you can use gridUid and difficulty to load the game
            loadTemplateFromFirestore(gridUid);
        }
    }

    private void loadTemplateFromFirestore(String gridUid) {
        db.collection("templates")
                .document(gridUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Convert the Firestore data to a Template object
                            template = convertDocumentToTemplate(document);

                            // Start the game now that the template is loaded
                            startGame();
                        } else {
                            // Handle the case where the document doesn't exist
                            Toast.makeText(this, "Template not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(this, "Failed to load template", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Template convertDocumentToTemplate(DocumentSnapshot document) {
        Template template = new Template(document.getLong("size").intValue());
        template.setGridUid(document.getString("gridUid"));
        template.setSolved(Boolean.TRUE.equals(document.getBoolean("isSolved")));
        // Load the grid
        java.util.List<Long> gridLongList = (java.util.List<Long>) document.get("grid");
        assert gridLongList != null;
        int[] grid = new int[gridLongList.size()];
        for (int i = 0; i < gridLongList.size(); i++) {
            grid[i] = gridLongList.get(i).intValue();
        }
        template.setGrid(grid);
        // Load the cellStates
        java.util.List<String> cellStatesStringList = (java.util.List<String>) document.get("cellStates");
        assert cellStatesStringList != null;
        Cell[] cellStates = new Cell[cellStatesStringList.size()];
        for (int i = 0; i < cellStatesStringList.size(); i++) {
            cellStates[i] = Cell.valueOf(cellStatesStringList.get(i));
        }
        template.setCellStates(cellStates);
        // Load rowHints
        java.util.List<java.util.List<Long>> rowHintsLongList = (java.util.List<java.util.List<Long>>) document.get("rowHints");
        assert rowHintsLongList != null;
        int[][] rowHints = new int[rowHintsLongList.size()][];
        for (int i = 0; i < rowHintsLongList.size(); i++) {
            java.util.List<Long> rowHint = rowHintsLongList.get(i);
            rowHints[i] = new int[rowHint.size()];
            for (int j = 0; j < rowHint.size(); j++) {
                rowHints[i][j] = rowHint.get(j).intValue();
            }
        }
        template.setRowHints(rowHints);
        // Load colHints
        java.util.List<java.util.List<Long>> colHintsLongList = (java.util.List<java.util.List<Long>>) document.get("colHints");
        assert colHintsLongList != null;
        int[][] colHints = new int[colHintsLongList.size()][];
        for (int i = 0; i < colHintsLongList.size(); i++) {
            java.util.List<Long> colHint = colHintsLongList.get(i);
            colHints[i] = new int[colHint.size()];
            for (int j = 0; j < colHint.size(); j++) {
                colHints[i][j] = colHint.get(j).intValue();
            }
        }
        template.setColHints(colHints);
        return template;
    }

    public void startGame() {
        // Display the game logic
        // Get the grid from the template
        int[] grid = template.getGrid();
        // Get the grid layout
        GridLayout gridLayout = findViewById(R.id.kakuroGridLayout);
        // Set the correct number of rows and columns
        gridLayout.setColumnCount(5);
        gridLayout.setRowCount(5);

        // Create all the cells
        for (int i = 0; i < template.getGrid().length; i++) {
            TextView cell = new TextView(this);
            cell.setText(String.valueOf(template.getGrid()[i]));
            //TODO set the cell depending on the type
            //TODO add the onClick function to select the cell
            gridLayout.addView(cell);
        }

        // Get references to number buttons
        Button numberButton1 = findViewById(R.id.numberButton1);
        Button numberButton2 = findViewById(R.id.numberButton2);
        Button numberButton3 = findViewById(R.id.numberButton3);
        Button numberButton4 = findViewById(R.id.numberButton4);
        Button numberButton5 = findViewById(R.id.numberButton5);
        Button numberButton6 = findViewById(R.id.numberButton6);
        Button numberButton7 = findViewById(R.id.numberButton7);
        Button numberButton8 = findViewById(R.id.numberButton8);
        Button numberButton9 = findViewById(R.id.numberButton9);

    }

    public void endGame() {
        //logic to end the game
    }

    public void displayMessage() {
        //logic to display a message
    }

    public void redirect_player_dashboard_activity() {
        //logic to go to the dashboard
    }
}