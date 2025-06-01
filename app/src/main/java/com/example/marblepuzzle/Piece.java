package com.example.marblepuzzle;

public class Piece {
    private String name;
    private float[] XY;
    private int[][] offset;
    private int rotate;
    private boolean mirrorVer, mirrorHor;


    public Piece(String name, float[] XY, int[][] offset, int rotate, boolean mirrorVer, boolean mirrorHor) {
        this.name = name;
        this.XY = XY;
        this.offset = offset;
        this.rotate = rotate;
        this.mirrorVer = mirrorVer;
        this.mirrorHor = mirrorHor;
    }

    public String getName() {
        return name;
    }

    public float[] getXY() {
        return XY;
    }

    public int getRotate() {
        return rotate;
    }

    public boolean isMirrorVer() {
        return mirrorVer;
    }

    public boolean isMirrorHor() {
        return mirrorHor;
    }

    public int[][] getOffset() {
        return offset;
    }

    public void setXY(float x, float y) {
        XY[0] = x;
        XY[1] = y;
    }

    public void setOffset(int[][] offset) {
        this.offset = offset;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public void setMirrorVer(boolean mirrorVer) {
        this.mirrorVer = mirrorVer;
    }

    public void setMirrorHor(boolean mirrorHor) {
        this.mirrorHor = mirrorHor;
    }



    public void rotate() {

    }

    public void mirrorVer() {

    }

    public void setMirrorHor() {

    }


}
