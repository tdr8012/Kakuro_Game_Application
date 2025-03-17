package com.example.kakurogamelatestversion;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Kakuro_App extends AppCompatActivity {

    private Game game;
    private Template template;
    private String difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakuro_app);

        // Get data passed from Level
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String gridUid = extras.getString("gridUid");
            difficulty = extras.getString("difficulty");
            // Now you can use gridUid and difficulty to load the game
            // Use gridUid to find the template
            //template=Template.getTemplateByUid(gridUid);
            // Use difficulty to know the game difficulty
            if(template == null) {
                Toast.makeText(this, "No template with this id", Toast.LENGTH_SHORT).show();
            }
            // Use your Game object to display the game
            startGame();
        }
    }
    public void startGame(){
        //logic to start the game
    }
    public void endGame(){
        //logic to end the game
    }
    public void displayMessage(){
        //logic to display a message
    }
    public void redirect_player_dashboard_activity(){
        //logic to go to the dashboard
    }
}