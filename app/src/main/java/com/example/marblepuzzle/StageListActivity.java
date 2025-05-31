package com.example.marblepuzzle;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentActivity;

public class StageListActivity extends FragmentActivity {
    StageListManager stageListManager;
    private GridLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_list);

        int difficulty = getIntent().getIntExtra("difficulty",0);
        stageListManager = new StageListManager(this, difficulty);
        container = findViewById(R.id.stageGrid);

        ImageButton difficultyButtonBack = findViewById(R.id.difficultyButtonBack);
        difficultyButtonBack.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        container.removeAllViews();
        stageListManager.addStageItem(this,container);
    }

}
