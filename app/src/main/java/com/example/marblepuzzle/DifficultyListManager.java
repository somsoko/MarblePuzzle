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

// 난이도 리스트 구성 클래스
public class DifficultyListManager {
    // 각 난이도 정보 저장 배열
    private ArrayList<DifficultyInfo> info = new ArrayList<>();
    private SharedPreferences pref;

    // 각 json 난이도 정보 읽기
    public DifficultyListManager(Context context) {
        pref = context.getSharedPreferences("difficultyInfo",Context.MODE_PRIVATE);
        int diffNum = pref.getInt("diffNum",-1);

        if(diffNum == -1) {
            AssetManager assetManager = context.getAssets();
            StringBuilder str = new StringBuilder();

            try {
                InputStream is = assetManager.open("difficultyInfo.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
                reader.close();
                is.close();

                JSONArray jsonArray = new JSONArray(str.toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                diffNum = jsonObject.getInt("diffNum");
                pref.edit().putInt("diffNum",diffNum).apply();

                for(int i=1; i<=diffNum; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    pref.edit().putString(i+"difficulty",jsonObject.getString(i+"difficulty")).apply();
                    pref.edit().putInt(i+"total",jsonObject.getInt(i+"total")).apply();
                    pref.edit().putInt(i+"cleared",jsonObject.getInt(i+"cleared")).apply();
                    pref.edit().putBoolean(i+"locked",jsonObject.getBoolean(i+"locked")).apply();
                }
            }
            catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setClickListener(Context context, View card, int i) {
        if(!info.get(i).getLocked()) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(context, StageListActivity.class);
                intent.putExtra("difficulty", (int) v.getTag());
                context.startActivity(intent);
            });
        }
    }

    private void unlockNext(int i,int diffNum) {
        if(i<diffNum-1)
            pref.edit().putBoolean((i+1)+"locked",false).apply();
    }

    public void refresh() {
        int diffNum = pref.getInt("diffNum",-1);
        info.clear();
        for(int i=1; i<=diffNum; i++) {
            int total = pref.getInt(i+"total",0);
            int cleared = pref.getInt(i+"cleared",0);
            if(total == cleared) {
                unlockNext(i,diffNum);
            }
            info.add(new DifficultyInfo(pref.getString(i+"difficulty",null),total,cleared,pref.getBoolean(i+"locked",true)));
        }
    }

    private void setCard(Context context, View card, DifficultyInfo inform) {
        if(!inform.getLocked()) {
            LinearLayout layout = card.findViewById(R.id.layout);
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_card_background));
            layout.setClickable(true);
            layout.setFocusable(true);
            layout.setAlpha(1);

            ImageView imageView = card.findViewById(R.id.lockedImage);
            imageView.setVisibility(View.GONE);

            TextView textView = card.findViewById(R.id.progressText);
            String progress = inform.getCleared()+" / "+ inform.getTotal();
            textView.setText(progress);
            textView.setVisibility(View.VISIBLE);

            ProgressBar bar = card.findViewById(R.id.progressBar);
            bar.setMax(inform.getTotal());
            bar.setProgress(inform.getCleared());
            bar.setVisibility(View.VISIBLE);
        }
    }

    public void addCard(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        for(int i=0; i<pref.getInt("diffNum",0); i++) {
            DifficultyInfo inform = info.get(i);

            // difficulty_card.xml을 뷰로 inflate
            View card = inflater.inflate(R.layout.difficulty_card, container, false);

            TextView textView = card.findViewById(R.id.difficultyText);
            textView.setText(textView.getResources().getIdentifier(inform.getDifficulty(),"string",context.getPackageName()));
            card.setTag(i+1);
            setCard(context,card,inform);

            setClickListener(context,card,i);

            container.addView(card);
        }
    }
}
