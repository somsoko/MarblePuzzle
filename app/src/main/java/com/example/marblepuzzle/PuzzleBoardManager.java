package com.example.marblepuzzle;

public class PuzzleBoardManager {
    private boolean[][] board = new boolean[10][];

    public PuzzleBoardManager() {
        for(int i=0; i<10; i++) {
            board[i] = new boolean[10-i];
        }
    }

    /*public int getX(int x) {
        int xIndex;
        return xIndex;
    }

    public int getY(int y) {
        int yIndex;
        return yIndex;
    }

    public boolean isValid(int xIndex, int yIndex) {

    }

    public void inPiece(int xIndex, int yIndex, int[][] offset) {

    }

    public void outPiece(int xIndex, int yIndex, int[][] offset) {

    }

    public boolean isClear() {

    }
*/
}
