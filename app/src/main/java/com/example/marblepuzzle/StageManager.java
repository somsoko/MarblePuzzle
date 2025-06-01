package com.example.marblepuzzle;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class StageManager {
    HashMap<String,Piece> piece = new HashMap<>();
    String[] usedPiece;

    public StageManager(Context context, String stageName) {
        AssetManager assetManager = context.getAssets();
        StringBuilder pieceInfo = new StringBuilder();

        try {
            InputStream is = assetManager.open("piece.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                pieceInfo.append(line);
            }
            reader.close();
            is.close();

            JSONArray jsonArray = new JSONArray(pieceInfo.toString());
            for(int i=0; i<12; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString("name");

                JSONArray originArray = jsonObject.getJSONArray("origin");
                float[] origin = new float[originArray.length()];
                for (int j = 0; j < originArray.length(); j++) {
                    origin[j] = originArray.getInt(j);
                }

                JSONArray offsetArray = jsonObject.getJSONArray("offset");
                int[][] offset = new int[offsetArray.length()][2];
                for (int j = 0; j < offsetArray.length(); j++) {
                    JSONArray pair = offsetArray.getJSONArray(j);
                    offset[j][0] = pair.getInt(0);
                    offset[j][1] = pair.getInt(1);
                }

                int rotate = jsonObject.getInt("rotate");

                boolean mirrorVer = jsonObject.getBoolean("mirrorVer");

                boolean mirrorHor = jsonObject.getBoolean("mirrorHor");

                piece.put(name,new Piece(name,origin,offset,rotate,mirrorVer,mirrorHor));
            }
        }
        catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

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

            JSONObject jsonObject = new JSONObject(str.toString());

            JSONArray usedArray = jsonObject.getJSONArray("used");
            usedPiece = new String[usedArray.length()];
            for (int j = 0; j < usedArray.length(); j++) {
                usedPiece[j] = usedArray.getString(j);
            }

            JSONArray placeArray = jsonObject.getJSONArray("originPlace");
            float[][] originPlace = new float[placeArray.length()][2];
            for (int j = 0; j < placeArray.length(); j++) {
                JSONArray pair = placeArray.getJSONArray(j);
                originPlace[j][0] = pair.getInt(0);
                originPlace[j][1] = pair.getInt(1);
            }

            JSONArray rotateArray = jsonObject.getJSONArray("originRotate");
            int[] originRotate = new int[rotateArray.length()];
            for (int j = 0; j < rotateArray.length(); j++) {
                originRotate[j] = rotateArray.getInt(j);
            }

            JSONArray mirrorVerArray = jsonObject.getJSONArray("originMirrorVer");
            boolean[] originMirrorVer = new boolean[mirrorVerArray.length()];
            for (int j = 0; j < mirrorVerArray.length(); j++) {
                originMirrorVer[j] = mirrorVerArray.getBoolean(j);
            }

            JSONArray mirrorHorArray = jsonObject.getJSONArray("originMirrorHor");
            boolean[] originMirrorHor = new boolean[mirrorHorArray.length()];
            for (int j = 0; j < mirrorHorArray.length(); j++) {
                originMirrorHor[j] = mirrorHorArray.getBoolean(j);
            }

            for(int i=0; i< usedPiece.length; i++) {
                Piece p = piece.get(usedPiece[i]);
                p.setXY(originPlace[i][0],originPlace[i][1]);
                p.setRotate(originRotate[i]);
                p.setMirrorVer(originMirrorVer[i]);
                p.setMirrorHor(originMirrorHor[i]);
            }
        }
        catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPiece(Context context, ViewGroup container){
        for(Piece p : piece.values()) {
            ImageView imageView = new ImageView(context);
            String name = p.getName();
            int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            imageView.setImageResource(id);
            float[] xy = p.getXY();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(310,300);
            params.leftMargin = (int) xy[0];
            params.topMargin = (int) xy[1];
            imageView.setLayoutParams(params);

            boolean used = false;
            for(int i=0; i<usedPiece.length; i++) {
                if(name.equals(usedPiece[i])) {
                    imageView.setAlpha(0.5F);
                    used = true;
                }
            }

            if(!used) {
                imageView.setOnTouchListener(new View.OnTouchListener() {
                    float dX, dY;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                dX = view.getX() - event.getRawX();
                                dY = view.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                float newX = event.getRawX() + dX;
                                float newY = event.getRawY() + dY;

                                view.animate()
                                        .x(event.getRawX() + dX)
                                        .y(event.getRawY() + dY)
                                        .setDuration(0)
                                        .start();

                                // 새로운 좌표 저장
                                p.setXY(newX, newY);

                                break;
                            /* 스냅 기능
                            case MotionEvent.ACTION_UP:
                                float viewX = view.getX();
                                float viewY = view.getY();

                                int[] snappedXY = findNearestHole(viewX, viewY); // 사용자 함수

                                view.animate()
                                        .x(snappedXY[0]) // 사용자 함수
                                        .y(snappedXY[1])
                                        .setDuration(100)
                                        .start();

                                // 위치 정보 저장
                                Piece piece = (Piece) view.getTag();
                                piece.setXY(snappedXY[0], snappedXY[1]);
                                break;
                             */
                        }
                        return true;
                    }
                });
            }

            container.addView(imageView);
        }

    }

    public void setTimer() {

    }

    public void setStar() {

    }





}
