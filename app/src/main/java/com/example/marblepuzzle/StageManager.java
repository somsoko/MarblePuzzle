package com.example.marblepuzzle;

import android.content.Context;
import android.content.res.AssetManager;
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
    private HashMap<String,Piece> piece = new HashMap<>();
    private String[] usedPiece;
    private PuzzleBoardManager board;

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
        board = new PuzzleBoardManager(container);
        for(Piece p : piece.values()) {
            ImageView imageView = new ImageView(context);
            String name = p.getName();
            int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            imageView.setImageResource(id);

            for(int i=0; i<p.getRotate()/90; i++) {
                p.rotate();
            }
            imageView.setRotation(p.getRotate());
            if(p.isMirrorVer()) {
                p.mirrorVer();
                imageView.setScaleY(-1f);
                imageView.setRotation(90);
            }
            if(p.isMirrorHor()) {
                p.mirrorHor();
                imageView.setScaleX(-1f);
                imageView.setRotation(90);
            }

            float[] xy = p.getXY();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(305,305);
            xy = board.getRealXY((int)xy[0],(int)xy[1]);
            float[] w = p.getCenter();
            params.leftMargin = (int)(xy[0]-2*w[0]);
            params.topMargin = (int)(xy[1]-2*w[1]);
            imageView.setLayoutParams(params);



            boolean used = false;
            for(int i=0; i<usedPiece.length; i++) {
                if(name.equals(usedPiece[i])) {
                    imageView.setAlpha(0.5F);
                    used = true;
                }
            }

            if(!used) {
                setTouchListener(imageView,p);
            }

            container.addView(imageView);
        }

    }

    public void setTouchListener(View imageView, Piece p) {
            imageView.setOnTouchListener(new View.OnTouchListener() {
                float dX, dY;
                float[] w = p.getCenter();

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = view.getX() - event.getRawX();
                            dY = view.getY() - event.getRawY();

                            view.bringToFront();
                            view.invalidate();

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
                            p.setXY(newX+2*w[0], newY+2*w[1]);

                            break;

                            case MotionEvent.ACTION_UP:
                                float viewX = view.getX()+2*w[0];
                                float viewY = view.getY()+2*w[1];

                                int[] logical = board.getLogicalXY(viewX, viewY);
                                int x = logical[0];
                                int y = logical[1];

                                if (x < 0 || x >= 10) {
                                    p.setXY(viewX, viewY);
                                    break;
                                }
                                if (y < 0 || y >= 10 - x) {
                                    p.setXY(viewX, viewY);
                                    break;
                                }

                                if(board.isValid(x,y)) {
                                    float[] xy = board.getRealXY(x,y);

                                    view.animate()
                                            .x(xy[0]-2*w[0])
                                            .y(xy[1]-2*w[1])
                                            .setDuration(100)
                                            .start();

                                    board.inPiece(x,y,p.getOffset());

                                    p.setXY(xy[0]-2*w[0], xy[1]-2*w[1]);
                                    break;
                                }

                                p.setXY(viewX, viewY);
                                break;

                    }
                    return true;
                }
            });
    }

    public void setTimer() {

    }

    public void setStar() {

    }





}
