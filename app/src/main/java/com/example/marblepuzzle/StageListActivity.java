package com.example.marblepuzzle;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

public class StageListActivity extends FragmentActivity {
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.stage_list);

        String difficulty = getIntent().getStringExtra("difficulty");

        TextView text = findViewById(R.id.stage);
        text.setText(difficulty);

        SharedPreferences pref = getSharedPreferences("difficultyInfo", Context.MODE_PRIVATE);
        int cleared = pref.getInt(difficulty, 7);
        TextView text2 = findViewById(R.id.clear);
        String s = cleared+"";
        text2.setText(s);
        pref.edit().putInt(difficulty,cleared+1).apply();
    }
}
