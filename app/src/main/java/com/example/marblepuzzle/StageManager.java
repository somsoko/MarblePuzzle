package com.example.marblepuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
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
    private ControlPiece controlPiece;
    private boolean clear;
    private Context context;
    private Timer timer;
    private SharedPreferences pref;
    private String diff;
    private String stage;

    public StageManager(Context context, String stageName, ViewGroup container, Timer timer) {
        this.context = context;
        this.timer = timer;
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
                float[] center = new float[2];
                center[0] = originArray.getInt(0);
                center[1] = originArray.getInt(1);

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

                JSONArray placeArray = jsonObject.getJSONArray("place");
                float[] place = new float[2];
                int x = placeArray.getInt(0);
                if(x<0) {
                    place[0] = container.getWidth()+x*305+2*center[0];
                }
                else {
                    place[0] = x*305+2*center[0];
                }
                place[1] = placeArray.getInt(1)*305+2*center[1];


                piece.put(name,new Piece(name,center,offset,rotate,mirrorVer,mirrorHor,place));
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

        String[] part = stageName.split("-");
        diff = part[0].trim();
        stage = part[1].trim();
        pref = context.getSharedPreferences(diff+"diff", Context.MODE_PRIVATE);
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
                if(imageView.getRotation()%180 == 90) {
                    imageView.setScaleX(imageView.getScaleX() * -1f);
                    imageView.setRotation(imageView.getRotation() + 90);
                }
                else {
                    imageView.setRotation(imageView.getRotation() + 90);
                    imageView.setScaleY(imageView.getScaleY() * -1f);
                }
            }
            if(p.isMirrorHor()) {
                p.mirrorHor();
                if(imageView.getRotation()%180 != 90) {
                    imageView.setScaleX(imageView.getScaleX() * -1f);
                    imageView.setRotation(imageView.getRotation() + 90);
                }
                else {
                    imageView.setRotation(imageView.getRotation() + 90);
                    imageView.setScaleY(imageView.getScaleY() * -1f);
                }
            }

            boolean used = false;
            for(int i=0; i<usedPiece.length; i++) {
                if(name.equals(usedPiece[i])) {
                    imageView.setAlpha(0.4F);
                    used = true;
                }
            }

            float[] xy = p.getXY();
            if(used) {
                board.inPiece((int)xy[0], (int)xy[1], p.getOffset());
                xy = board.getPhysicalXY((int) xy[0], (int) xy[1]);
            }
            else
                setTouchListener(imageView,p,context,container);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(305,305);
            float[] w = p.getCenter();
            int x = (int)(xy[0]-2*w[0]);
            int y = (int)(xy[1]-2*w[1]);
            params.leftMargin = x;
            params.topMargin = y;
            imageView.setLayoutParams(params);

            container.addView(imageView);
        }

        board.copyBoard();
    }

    private void setTouchListener(View imageView, Piece p, Context context, ViewGroup container) {
        controlPiece = new ControlPiece(context,container);

            container.setOnClickListener(v -> controlPiece.hideControlPiece());

            imageView.setOnTouchListener(new View.OnTouchListener() {
                float dX, dY;
                float startX, startY;
                float[] w = p.getCenter();


                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = view.getX();
                            startY = view.getY();
                            dX = view.getX() - event.getRawX();
                            dY = view.getY() - event.getRawY();

                            int[] outLogical = board.getLogicalXY(view.getX()+2*w[0], view.getY()+2*w[1]);
                            int outX = outLogical[0];
                            int outY = outLogical[1];

                            if(board.isIn(outX,outY,p.getOffset())) {
                                board.outPiece(outX,outY,p.getOffset());
                            }

                            view.bringToFront();
                            view.invalidate();

                            controlPiece.hideControlPiece();

                            break;

                        case MotionEvent.ACTION_MOVE:
                            float rawX = event.getRawX();
                            float rawY = event.getRawY();
                            float newX = rawX + dX;
                            float newY = rawY + dY;

                            int viewWidth = view.getWidth();
                            int viewHeight = view.getHeight();
                            int maxX = container.getWidth() - viewWidth;
                            int maxY = container.getHeight() - viewHeight;

                            float limitedX = Math.max(0, Math.min(newX, maxX));
                            float limitedY = Math.max(0, Math.min(newY, maxY));

                            view.animate()
                                    .x(limitedX)
                                    .y(limitedY)
                                    .setDuration(0)
                                    .start();

                            // 새로운 좌표 저장
                            p.setXY(limitedX+2*w[0], limitedY+2*w[1]);

                            break;

                            case MotionEvent.ACTION_UP:
                                float viewX = view.getX()+2*w[0];
                                float viewY = view.getY()+2*w[1];

                                int[] logical = board.getLogicalXY(viewX, viewY);
                                int x = logical[0];
                                int y = logical[1];

                                if(board.isValid(x,y,p.getOffset())) {
                                    float[] xy = board.getPhysicalXY(x,y);

                                    view.animate()
                                            .x(xy[0]-2*w[0])
                                            .y(xy[1]-2*w[1])
                                            .setDuration(100)
                                            .start();

                                    board.inPiece(x,y,p.getOffset());

                                    if(isClear()) {
                                        puzzleComplete();
                                    }

                                    p.setXY(xy[0]-2*w[0], xy[1]-2*w[1]);
                                    break;
                                }
                                else {
                                    if(board.isIn(x,y,p.getOffset())) {
                                        view.animate()
                                                .x(startX)
                                                .y(startY)
                                                .setDuration(100)
                                                .start();

                                        logical = board.getLogicalXY(startX+2*w[0],startY+2*w[1]);
                                        if(board.isValid(logical[0],logical[1], p.getOffset())) {
                                            board.inPiece(logical[0],logical[1],p.getOffset());
                                        }

                                        p.setXY(startX, startY);
                                    }
                                    else {
                                        controlPiece.getFocus(p,imageView);
                                        controlPiece.showControlPiece(viewX,viewY-2*67);
                                        p.setXY(viewX, viewY);
                                    }
                                }

                                break;

                    }
                    return true;
                }
            });
    }

    private boolean isClear() {
        clear = board.isClear();
        return clear;
    }

    private void puzzleComplete() {
        timer.pause();  // 시간 멈춤

        long elapsedTime = timer.getElapsedMillis(); // 밀리초
        int stars = setStar(elapsedTime); // 시간에 따른 별 갯수 계산

        showClearDialog(stars);  // 클리어 창 띄움
    }

    private int setStar(long elapsedMillis) {
        if (elapsedMillis < 120000) return 3;
        else if (elapsedMillis < 240000) return 2;
        else return 1;
    }

    private void showClearDialog(int stars) {
        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = inflater.inflate(R.layout.clear, null);
        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        // 별 갯수 표시
        ImageView[] starViews = {
                view.findViewById(R.id.clear_star1),
                view.findViewById(R.id.claer_star2),
                view.findViewById(R.id.clear_star3)
        };

        for (int i = 0; i < 3; i++) {
            if (i < stars) {
                starViews[i].setImageResource(R.drawable.cleared_star);  // 획득한 별
            }
            else {
                starViews[i].setImageResource(R.drawable.star); // 획득 못한 별
            }
        }

        int originStar = pref.getInt(stage+"star",0);
        pref.edit().putInt(stage+"star",Math.max(originStar,stars)).apply();

        TextView text = view.findViewById(R.id.clearLevel);
        String stageName = diff+" - "+stage;
        text.setText(stageName);

        TextView textView = view.findViewById(R.id.clearTimer);
        String time = "걸린 시간 : " + timer.getElapsedMillis()/1000 + "초";
        textView.setText(time);

        view.findViewById(R.id.clearLevelSelect).setOnClickListener(v -> {
            dialog.dismiss();
            ((Activity)context).finish(); // 레벨 선택 화면
        });

        view.findViewById(R.id.clearRetry).setOnClickListener(v -> {
            Intent intent = ((Activity)context).getIntent();
            ((Activity)context).finish();
            (context).startActivity(intent);
        });
    }
}
