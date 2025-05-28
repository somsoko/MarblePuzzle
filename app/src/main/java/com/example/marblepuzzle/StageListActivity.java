package com.example.marblepuzzle;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

public class StageListActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_list);

        String difficulty = getIntent().getStringExtra("difficulty");

        TextView text = findViewById(R.id.stage);
        text.setText(difficulty);
    }
}
