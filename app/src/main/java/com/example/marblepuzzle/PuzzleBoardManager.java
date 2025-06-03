package com.example.marblepuzzle;

import android.view.View;
import android.widget.ImageView;

public class PuzzleBoardManager {
    private boolean[][] board = new boolean[10][];
    private ImageView imageView;

    public PuzzleBoardManager(View container) {
        for(int i=0; i<10; i++) {
            board[i] = new boolean[10-i];
        }

        imageView = container.findViewById(R.id.board);
    }

    // 논리 좌표를 실제 좌표로 변환
    public float[] getRealXY(int x, int y) {
        float[] xy = new float[2];
        int w = imageView.getWidth();
        int h = imageView.getHeight();
        float r = h/15.2f;
        xy[0] = (imageView.getX()+w/2f)+(r*x)-(r*y);
        xy[1] = (imageView.getY()+h/11f)+(r*x)+(r*y);

        return xy;
    }

    // 실제 좌표를 논리 좌표로 변환
    public int[] getLogicalXY(float screenX, float screenY) {
        int w = imageView.getWidth();
        int h = imageView.getHeight();
        float r = h/15.2f;
        float centerX = imageView.getX() + w / 2f;
        float centerY = imageView.getY() + h / 11f;

        float sx = screenX - centerX;
        float sy = screenY - centerY;

        float denom = (r * r + r * r);

        float x = (sx * r + sy * r) / denom;
        float y = (sy * r - sx * r) / denom;

        // 반올림하여 가장 가까운 논리 좌표로
        return new int[]{Math.round(x), Math.round(y)};
    }

    public boolean isValid(int xIndex, int yIndex) {
        return true;
    }

    public void inPiece(int xIndex, int yIndex, int[][] offset) {

    }

    public void outPiece(int xIndex, int yIndex, int[][] offset) {

    }

    public boolean isClear() {
        return true;
    }

}
