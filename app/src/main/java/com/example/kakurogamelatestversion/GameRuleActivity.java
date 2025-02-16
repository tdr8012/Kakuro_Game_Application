package com.example.kakurogamelatestversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameRuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rule);

        // Set game rules content
        TextView rulesText = findViewById(R.id.rulesText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button goBackBtn = findViewById(R.id.goBackBtn);

        rulesText.setText(getGameRules());

        goBackBtn.setOnClickListener(view -> navigateToPLayerDashboardActivity());
    }
    private void navigateToPLayerDashboardActivity() {
        startActivity(new Intent(GameRuleActivity.this, PlayerDashboardActivity.class));

    }
    private Spanned getGameRules() {
        return Html.fromHtml(
                "<b>📌 Kakuro Game Introduction</b><br/><br/>" +

                        "<b>🔢 Basic Rules:</b><br/>" +
                        "1️⃣ The board consists of <b>empty white squares</b> and <b>black squares</b> that contain numbers.<br/>" +
                        "2️⃣ The numbers in black squares indicate the <b>sum</b> of the white squares in the row or column.<br/>" +
                        "3️⃣ Each row and column <b>must contain unique digits (1-9)</b> with no repetition.<br/>" +
                        "4️⃣ The objective is to fill all white squares while satisfying all sum constraints.<br/><br/>" +

                        "<b>🧩 Example:</b><br/>" +
                        " If a row has two white squares and the black square shows <b>'4'</b>, possible values could be <b>(1,3) or (3,1)</b>.<br/>" +
                        " If a column has three white squares with a sum of <b>'6'</b>, possible values are <b>(1,2,3) or (3,2,1)</b>.<br/><br/>" +

                        "<b>🎯 Winning the Game:</b><br/>" +
                        " You win when all white squares are correctly filled without breaking the sum constraints.<br/>" +
                        " <b>No number</b> should repeat in the same row or column group.<br/><br/>" +

                        "<b>🕹️ Enjoy playing Kakuro and train your brain!</b>"
        );
    }
}
