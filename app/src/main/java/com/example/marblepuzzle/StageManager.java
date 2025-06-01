package com.example.marblepuzzle;

import android.content.Context;
import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StageManager {
    public StageManager(Context context, String stageName) {
        AssetManager assetManager = context.getAssets();
        StringBuilder str = new StringBuilder();

        try {
            InputStream is = assetManager.open(stageName+".json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
            is.close();

            JSONArray jsonArray = new JSONArray(str.toString());
            for(int i=1; i<=12; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

            }
        }
        catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
