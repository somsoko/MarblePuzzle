package com.example.marblepuzzle;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentActivity;


    public class StageActivity extends FragmentActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.stage);

            ImageButton stageButton = findViewById(R.id.stageButtonBack);
            stageButton.setOnClickListener(view -> {
                finish();
            });

            String stage = getIntent().getStringExtra("stageName");
            String[] part = stage.split("-");
            String diff = part[0].trim();
            String stageName = part[1].trim();
            SharedPreferences pref = getSharedPreferences(diff+"diff", Context.MODE_PRIVATE);
            pref.edit().putInt(stageName+"star",1).apply();
        }
    }

