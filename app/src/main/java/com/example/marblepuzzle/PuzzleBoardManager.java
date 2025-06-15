package com.example.marblepuzzle;

import android.view.View;
import android.widget.ImageView;

public class PuzzleBoardManager {
    private boolean[][] board = new boolean[10][];
    private boolean[][] fixed = new boolean[10][];
    private ImageView imageView;

    public PuzzleBoardManager(View container) {
        for(int i=0; i<10; i++) {
            board[i] = new boolean[10-i];
            fixed[i] = new boolean[10-i];
        }

        imageView = container.findViewById(R.id.board);
    }


    // debug용 함수
    public String printBoard() {
        String s ="";
        for(int i=0; i<10; i++) {
            for(int j=0; j<10-i; j++) {
                if(board[i][j])
                    s += 1;
                else
                    s += 0;
            }
            s += "\n";
        }
        return s;
    }

    public void copyBoard() {
        for(int i=0; i<10; i++) {
            for(int j=0; j<10-i; j++) {
                fixed[i][j] = board[i][j];
            }
        }
    }

    // 논리 좌표를 실제 좌표로 변환
    public float[] getPhysicalXY(int x, int y) {
        float[] xy = new float[2];
        int w = imageView.getWidth();
        int h = imageView.getHeight();
        float r = h/15.2f;
        xy[0] = (imageView.getX()+w/2f)-(r*x)+(r*y);
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

        float x = (sy * r - sx * r) / denom;
        float y = (sy * r + sx * r) / denom;

        // 반올림하여 가장 가까운 논리 좌표로
        return new int[]{Math.round(x), Math.round(y)};
    }

    public boolean isValid(int x, int y, int[][] offset) {
        if (y < 0 || y >= 10) {
            return false;
        }
        if (x < 0 || x >= 10 - y) {
            return false;
        }
        if(board[x][y]){
            return false;
        }

        for(int i=0; i<offset.length; i++) {
            int offsetX = x-offset[i][1];
            int offsetY = y+offset[i][0];
            if (offsetY < 0 || offsetY >= 10) {
                return false;
            }
            if (offsetX < 0 || offsetX >= 10 - offsetY) {
                return false;
            }

            if(board[offsetX][offsetY]) {
                return false;
            }
        }

        return true;
    }

    public boolean isIn(int x, int y, int[][] offset) {
        if (y < 0 || y >= 10) {
            return false;
        }
        if (x < 0 || x >= 10 - y) {
            return false;
        }
        if(fixed[x][y]) {
            return false;
        }

        for(int i=0; i<offset.length; i++) {
            int offsetX = x - offset[i][1];
            int offsetY = y + offset[i][0];
            if (offsetY < 0 || offsetY >= 10) {
                return false;
            }
            if (offsetX < 0 || offsetX >= 10 - offsetY) {
                return false;
            }
            if(fixed[offsetX][offsetY]) {
                return false;
            }
        }

        return true;
    }

    public void inPiece(int x, int y, int[][] offset) {
        board[x][y] = true;

        for(int i=0; i<offset.length; i++) {
            int offsetX = x-offset[i][1];
            int offsetY = y+offset[i][0];
            board[offsetX][offsetY] = true;
        }
    }

    public void outPiece(int x, int y, int[][] offset) {
        board[x][y] = false;

        for(int i=0; i<offset.length; i++) {
            int offsetX = x-offset[i][1];
            int offsetY = y+offset[i][0];
            board[offsetX][offsetY] = false;
        }
    }

    public boolean isClear() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}
