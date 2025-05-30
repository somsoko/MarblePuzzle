package com.example.marblepuzzle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StageListManager {
    private SharedPreferences pref;
    private ArrayList<StageInfo> stageList = new ArrayList<>();

    public StageListManager(Context context, String difficulty) {
        pref = context.getSharedPreferences(difficulty,Context.MODE_PRIVATE);
        int stageNum = pref.getInt("stageNum",-1);

        if(stageNum == -1) {
            AssetManager assetManager = context.getAssets();
            StringBuilder str = new StringBuilder();

            try {
                InputStream is = assetManager.open(difficulty +".json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
                reader.close();
                is.close();

                JSONArray jsonArray = new JSONArray(str.toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String stageName = jsonObject.getString("stageName");
                stageNum = jsonObject.getInt("stageNum");
                pref.edit().putInt("stageNum",stageNum).apply();

                for(int i=1; i<=stageNum; i++) {
                    pref.edit().putString(i+"stage",stageName+" - "+i).apply();
                }

                for(int i=1; i<=stageNum; i++) {
                    //jsonObject = jsonArray.getJSONObject(i);

                }
            }
            catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public int setStageItem(Context context, View stage, int i, int cleared) {
        int star = pref.getInt(i+"star",0);
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

        for(int i=1; i<=pref.getInt("stageNum",0); i++) {
            // stage_item.xml을 뷰로 inflate
            View stageItem = inflater.inflate(R.layout.stage_item, container, false);

            TextView textView = stageItem.findViewById(R.id.stageText);
            textView.setText(pref.getString(i+"stage",null));
            stageItem.setTag(i+"stage");
            cleared = setStageItem(context,stageItem,i,cleared);

            container.addView(stageItem);
        }

        setCleared(context, cleared);
    }

    public void setCleared(Context context, int cleared) {
        SharedPreferences diff = context.getSharedPreferences("difficultyInfo",Context.MODE_PRIVATE);
        diff.edit().putInt(pref.getString("stageName",null),cleared).apply();
    }


}
