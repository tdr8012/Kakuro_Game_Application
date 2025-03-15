package com.example.kakurogamelatestversion;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
    // Attributes
    private boolean gameState; // true = on, false = off
    private Timer gameTimer;
    private long gameTimerSeconds;
    private boolean gameHint;
    private boolean gameComplete;

    // Constructor
    public Game() {
        this.gameState = false; // Default: game is off
        this.gameTimer = null;
        this.gameTimerSeconds = 0; // Start at 0
        this.gameHint = false; // Default: hints are off
        this.gameComplete = false; // Default: game not completed
    }

    // Methods

    // Game State Control
    public void setGameStateOn() {
        this.gameState = true;
    }

    public void setGameStateOff() {
        this.gameState = false;
    }

    // Game Timer Control
    public void setTimerOn() {
        if (gameTimer != null) {
            gameTimer.cancel(); // Stop any existing timer
        }

        gameTimer = new Timer();
        gameTimerSeconds=0;
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(gameState){
                    gameTimerSeconds++;
                    System.out.println("Time elapsed: " + gameTimerSeconds + " seconds");
                }
                // Update UI or perform other actions with gameTimerSeconds
            }
        }, 0, 1000); // Start immediately, repeat every 1000ms (1 second)
    }

    public void setTimerOff() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer.purge();
            gameTimer = null;
        }
    }

    // Game Hint Control
    public void setHintOn() {
        this.gameHint = true;
    }

    public void setHintOff() {
        this.gameHint = false;
    }

    // Check Game Timer
    public void checkGameTimer() {
        //We are not validating if it has reached the end, it is infinite
    }

    // Game Complete
    public void setGameComplete() {
        this.gameComplete = true;
        setTimerOff(); // Stop the timer
        setGameStateOff(); // Stop the game
        // Additional actions on game completion
    }
    //Getters
    public boolean getGameState(){
        return this.gameState;
    }
    public boolean getGameHint(){
        return this.gameHint;
    }
    public long getGameTimerSeconds(){
        return this.gameTimerSeconds;
    }
    public boolean getGameComplete(){
        return this.gameComplete;
    }
    public void displayMessage(String message) {
        System.out.println(message);
    }
}