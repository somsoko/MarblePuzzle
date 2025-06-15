package com.example.marblepuzzle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class StageListManager {
    private SharedPreferences diffPref;
    private SharedPreferences stagePref;
    private int diff;

    public StageListManager(Context context, int difficulty) {
        diffPref = context.getSharedPreferences("difficultyInfo",Context.MODE_PRIVATE);
        stagePref = context.getSharedPreferences(difficulty+"diff",Context.MODE_PRIVATE);
        diff = difficulty;
    }



    private int setStageItem(Context context, View stage, int i, int cleared) {
        int star = stagePref.getInt(i+"star",0);
        if(star >= 1) {
            ImageView imageView = stage.findViewById(R.id.star1);
            imageView.setImageResource(R.drawable.cleared_star);
            cleared++;
        }
        if(star >= 2) {
            ImageView imageView = stage.findViewById(R.id.star2);
            imageView.setImageResource(R.drawable.cleared_star);
        }
        if(star >= 3) {
            ImageView imageView = stage.findViewById(R.id.star3);
            imageView.setImageResource(R.drawable.cleared_star);
        }

        stage.setOnClickListener(v -> {
            Intent intent = new Intent(context, StageActivity.class);
            intent.putExtra("stageName", (String) v.getTag());
            context.startActivity(intent);
        });

        return cleared;
    }

    public void addStageItem(Context context, ViewGroup container) {
        int cleared = 0;
        LayoutInflater inflater = LayoutInflater.from(context);

        for(int i = 1; i<= diffPref.getInt(diff+"total",0); i++) {
            // stage_item.xml을 뷰로 inflate
            View stageItem = inflater.inflate(R.layout.stage_item, container, false);

            TextView textView = stageItem.findViewById(R.id.stageText);
            String stageName = diff+" - "+i;
            textView.setText(stageName);
            stageItem.setTag(stageName);
            cleared = setStageItem(context,stageItem,i,cleared);

            container.addView(stageItem);
        }

        setCleared(cleared);
    }

    private void setCleared(int cleared) {
        diffPref.edit().putInt(diff+"cleared",cleared).apply();
    }


}
