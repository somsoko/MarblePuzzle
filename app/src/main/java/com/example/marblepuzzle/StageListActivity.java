package com.example.marblepuzzle;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentActivity;

public class StageListActivity extends FragmentActivity {
    private StageListManager stageListManager;
    private GridLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.stage_list);

        int difficulty = getIntent().getIntExtra("difficulty",0);
        stageListManager = new StageListManager(this, difficulty);
        container = findViewById(R.id.stageGrid);

        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int screenWidthPx = displayMetrics.widthPixels;
        float density = displayMetrics.density;
        float screenWidthDp = screenWidthPx / density;
        int columnItemWidthDp = 120;  // 원하는 한 칸 너비(dp)
        int columnCount = Math.max((int)(screenWidthDp / columnItemWidthDp), 4); // 열 개수 계산
        container.setColumnCount(columnCount);  // GridLayout에 열 수 설정

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
