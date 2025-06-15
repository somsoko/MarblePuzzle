package com.example.marblepuzzle;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;


    public class StageActivity extends FragmentActivity {
        private StageManager stageManager;
        private Timer timer = new Timer();
        private String stage;
        private AlertDialog pauseDialog;
        private SharedPreferences pref;
        private String diff;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.stage);

            timer.start();

            ImageButton stageButton = findViewById(R.id.stageButtonBack);
            stageButton.setOnClickListener(view -> {
                timer.pause();
                showPauseOverlay();
            });

            String stageName = getIntent().getStringExtra("stageName");
            FrameLayout container = findViewById(R.id.stageWindow);

            container.post(()->{
            stageManager = new StageManager(this,stageName,container,timer);
                stageManager.addPiece(this,container);
            });

            String[] part = stageName.split("-");
            diff = part[0].trim();
            stage = part[1].trim();
            pref = getSharedPreferences(diff+"diff", Context.MODE_PRIVATE);
        }


        private void showPauseOverlay() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.pause, null);
            builder.setView(view);
            builder.setCancelable(false);
            pauseDialog = builder.create();
            pauseDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            pauseDialog.show();


            TextView tvLevel = view.findViewById(R.id.tvLevel);
            tvLevel.setText(stage);

            int star = pref.getInt(stage+"star",0);
            if(star >= 1) {
                ((ImageView)view.findViewById(R.id.star1)).setImageResource(R.drawable.cleared_star);
            }
            if(star >= 2) {
                ((ImageView)view.findViewById(R.id.star2)).setImageResource(R.drawable.cleared_star);
            }
            if(star >= 3) {
                ((ImageView)view.findViewById(R.id.star3)).setImageResource(R.drawable.cleared_star);;
            }

            view.findViewById(R.id.btnResume).setOnClickListener(v -> {
                pauseDialog.dismiss();
                timer.resume(); // 스톱워치 재개
            });

            view.findViewById(R.id.btnLevelSelect).setOnClickListener(v -> {
                timer.reset();
                finish();
            });

            view.findViewById(R.id.btnRetry).setOnClickListener(v -> {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            });
        }

        @Override
        protected void onDestroy() {
            if (pauseDialog != null && pauseDialog.isShowing()) {
                pauseDialog.dismiss();
            }
            super.onDestroy();
        }
    }

