package com.example.marblepuzzle;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.fragment.app.FragmentActivity;

public class DifficultySelectActivity extends FragmentActivity {
    private DifficultyListManager difficultyListManager;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.difficulty);

        difficultyListManager = new DifficultyListManager(this);
        container = findViewById(R.id.difficultyList);

        ImageButton difficultyButtonBack = findViewById(R.id.difficultyButtonBack);
        difficultyButtonBack.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        difficultyListManager.refresh();
        container.removeAllViews();
        difficultyListManager.addCard(this, container);
    }

}
