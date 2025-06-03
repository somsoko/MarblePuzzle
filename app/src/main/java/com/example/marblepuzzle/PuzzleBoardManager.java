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
        xy[0] = 1175+(61*x)-(61*y);
        xy[1] = 85+(60*x)+(61*y);
        return xy;
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
