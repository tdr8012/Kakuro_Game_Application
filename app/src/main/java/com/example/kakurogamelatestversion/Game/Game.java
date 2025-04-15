package com.example.kakurogamelatestversion.Game;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

public class Game {
    private CountDownTimer countDownTimer;
    private long totalTime = 5 * 60 * 1000;
    private boolean timerRunning = false;
    private int remainingHints = 0;

    public void setTimerOn(Context context, TextView timerTextView, Runnable onFinishCallback) {
        timerRunning = true;
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                timerTextView.setText("Timer: " + minutes + ":" + String.format("%02d", remainingSeconds));
            }

            public void onFinish() {
                timerTextView.setText("Timer: 0:00");
                Toast.makeText(context, "Time's up!", Toast.LENGTH_SHORT).show();
                timerRunning = false;
                onFinishCallback.run();
            }
        };
        countDownTimer.start();
    }

    public void setTimerOff() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }

    public void setHintOn(int hintCount) {
        remainingHints = hintCount;
    }

    public void setHintOff() {
        remainingHints = 0;
    }

    public int getRemainingHints() {
        return remainingHints;
    }

    public void consumeHint() {
        if (remainingHints > 0) remainingHints--;
    }

    public boolean hasHints() {
        return remainingHints > 0;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }
}

