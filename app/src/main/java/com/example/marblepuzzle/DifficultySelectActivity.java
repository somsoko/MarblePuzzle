package com.example.marblepuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.fragment.app.FragmentActivity;

public class DifficultySelectActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty);

        LinearLayout tutorialCard = findViewById(R.id.tutorialCard);
        tutorialCard.setOnClickListener(view -> {
            Intent intent = new Intent(this, StageListActivity.class);
            intent.putExtra("difficulty", "tutorial");
            startActivity(intent);
        });

        LinearLayout easyCard = findViewById(R.id.easyCard);
        easyCard.setOnClickListener(view -> {
            if(easyCard.isClickable()) {
                Intent intent = new Intent(this, StageListActivity.class);
                intent.putExtra("difficulty", "easy");
                startActivity(intent);
            }
        });

        LinearLayout normalCard = findViewById(R.id.normalCard);
        normalCard.setOnClickListener(view -> {
            if(normalCard.isClickable()) {
                Intent intent = new Intent(this, StageListActivity.class);
                intent.putExtra("difficulty", "normal");
                startActivity(intent);
            }
        });

        LinearLayout hardCard = findViewById(R.id.hardCard);
        hardCard.setOnClickListener(view -> {
            Intent intent = new Intent(this, StageListActivity.class);
            intent.putExtra("difficulty", "hard");
            startActivity(intent);
        });



    }
}
